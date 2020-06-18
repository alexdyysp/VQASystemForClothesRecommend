/**
 * Created by lycheng on 2019/8/1.
 * 
 * 语音听写流式 WebAPI 接口调用示例 接口文档（必看）：https://doc.xfyun.cn/rest_api/语音听写（流式版）.html
 * webapi 听写服务参考帖子（必看）：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=38947&extra=
 * 语音听写流式WebAPI 服务，热词使用方式：登陆开放平台https://www.xfyun.cn/后，找到控制台--我的应用---语音听写---个性化热词，上传热词
 * 注意：热词只能在识别的时候会增加热词的识别权重，需要注意的是增加相应词条的识别率，但并不是绝对的，具体效果以您测试为准。
 * 错误码链接：
 * https://www.xfyun.cn/doc/asr/voicedictation/API.html#%E9%94%99%E8%AF%AF%E7%A0%81
 * https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 * 语音听写流式WebAPI 服务，方言或小语种试用方法：登陆开放平台https://www.xfyun.cn/后，在控制台--语音听写（流式）--方言/语种处添加
 * 添加后会显示该方言/语种的参数值
 * 
 */
 
// 音频转码worker
let recorderWorker = new Worker('./js/transformpcm.worker.js')
// 记录处理的缓存音频
let buffer = []
let AudioContext = window.AudioContext || window.webkitAudioContext
let notSupportTip = '请试用chrome浏览器且域名为localhost或127.0.0.1测试'
navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia

recorderWorker.onmessage = function (e) {
  buffer.push(...e.data.buffer)
}

class IatRecorder {
  constructor (config) {
    this.config = config
    this.state = 'ing'
    this.language = config.language || 'zh_cn'
    this.accent = config.accent || 'mandarin'

    //以下信息在控制台-我的应用-语音听写（流式版）页面获取
    this.appId = '5decabfa'
    this.apiKey = '32a07d1d1610fff5acf64574af0ef7d3'
    this.apiSecret = '67f69967c221006b2a06aa7911783eab'
  }

  start () {
    this.stop()
    if (navigator.getUserMedia && AudioContext) {
      this.state = 'ing'
      if (!this.recorder) {
        var context = new AudioContext()
        this.context = context
        this.recorder = context.createScriptProcessor(0, 1, 1)

        var getMediaSuccess = (stream) => {
          var mediaStream = this.context.createMediaStreamSource(stream)
          this.mediaStream = mediaStream
          this.recorder.onaudioprocess = (e) => {
            this.sendData(e.inputBuffer.getChannelData(0))
          }
          this.connectWebsocket()
        }
        var getMediaFail = (e) => {
          this.recorder = null
          this.mediaStream = null
          this.context = null
          console.log('请求麦克风失败')
        }
        if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
          navigator.mediaDevices.getUserMedia({
            audio: true,
            video: false
          }).then((stream) => {
            getMediaSuccess(stream)
          }).catch((e) => {
            getMediaFail(e)
          })
        } else {
          navigator.getUserMedia({
            audio: true,
            video: false
          }, (stream) => {
            getMediaSuccess(stream)
          }, function (e) {
            getMediaFail(e)
          })
        }
      } else {
        this.connectWebsocket()
      }
    } else {
      var isChrome = navigator.userAgent.toLowerCase().match(/chrome/)
      alert(notSupportTip)
    }
  }

  stop () {
    this.state = 'end'
    try {
      this.mediaStream.disconnect(this.recorder)
      this.recorder.disconnect()
    } catch (e) {}
  }

  sendData (buffer) {
    recorderWorker.postMessage({
      command: 'transform',
      buffer: buffer
    })
  }
  connectWebsocket () {
    var url = 'wss://iat-api.xfyun.cn/v2/iat'
    var host = 'iat-api.xfyun.cn'
    var apiKey = this.apiKey
    var apiSecret = this.apiSecret
    var date = new Date().toGMTString()
    var algorithm = 'hmac-sha256'
    var headers = 'host date request-line'
    var signatureOrigin = `host: ${host}\ndate: ${date}\nGET /v2/iat HTTP/1.1`
    var signatureSha = CryptoJS.HmacSHA256(signatureOrigin, apiSecret)
    var signature = CryptoJS.enc.Base64.stringify(signatureSha)
    var authorizationOrigin = `api_key="${apiKey}", algorithm="${algorithm}", headers="${headers}", signature="${signature}"`
    var authorization = btoa(authorizationOrigin)
    url = `${url}?authorization=${authorization}&date=${date}&host=${host}`
    if ('WebSocket' in window) {
      this.ws = new WebSocket(url)
    } else if ('MozWebSocket' in window) {
      this.ws = new MozWebSocket(url)
    } else {
      alert(notSupportTip)
      return null
    }
    this.ws.onopen = (e) => {
      this.mediaStream.connect(this.recorder)
      this.recorder.connect(this.context.destination)
      setTimeout(() => {
        this.wsOpened(e)
      }, 500)
      this.config.onStart && this.config.onStart(e)
    }
    this.ws.onmessage = (e) => {
      this.config.onMessage && this.config.onMessage(e)
      this.wsOnMessage(e)
    }
    this.ws.onerror = (e) => {
      this.stop()
      this.config.onError && this.config.onError(e)
    }
    this.ws.onclose = (e) => {
      this.stop()
      this.config.onClose && this.config.onClose(e)
    }
  }

  wsOpened () {
    if (this.ws.readyState !== 1) {
      return
    }
    var audioData = buffer.splice(0, 1280)
    console.log('audioData', audioData.length)
    var params = {
      'common': {
        'app_id': this.appId
      },
      'business': {
        'language': this.language, //小语种可在控制台--语音听写（流式）--方言/语种处添加试用
        'domain': 'iat',
        'accent': this.accent, //中文方言可在控制台--语音听写（流式）--方言/语种处添加试用
        'vad_eos': 5000,
        'dwa': 'wpgs' //为使该功能生效，需到控制台开通动态修正功能（该功能免费）
      },
      'data': {
        'status': 0,
        'format': 'audio/L16;rate=16000',
        'encoding': 'raw',
        'audio': this.ArrayBufferToBase64(audioData)
      }
    }
    this.ws.send(JSON.stringify(params))
    this.handlerInterval = setInterval(() => {
      // websocket未连接
      if (this.ws.readyState !== 1) {
        clearInterval(this.handlerInterval)
        return
      }
      if (buffer.length === 0) {
        if (this.state === 'end') {
          this.ws.send(JSON.stringify({
            'data': {
              'status': 2,
              'format': 'audio/L16;rate=16000',
              'encoding': 'raw',
              'audio': ''
            }
          }))
          clearInterval(this.handlerInterval)
        }
        return false
      }
      audioData = buffer.splice(0, 1280)
      // 中间帧
      this.ws.send(JSON.stringify({
        'data': {
          'status': 1,
          'format': 'audio/L16;rate=16000',
          'encoding': 'raw',
          'audio': this.ArrayBufferToBase64(audioData)
        }
      }))
    }, 40)
  }

  wsOnMessage (e) {
    let jsonData = JSON.parse(e.data)
    // 识别结束
    if (jsonData.code === 0 && jsonData.data.status === 2) {
      this.ws.close()
    }
    if (jsonData.code !== 0) {
      this.ws.close()
      console.log(`${jsonData.code}:${jsonData.message}`)
    }
  }

  ArrayBufferToBase64 (buffer) {
    var binary = ''
    var bytes = new Uint8Array(buffer)
    var len = bytes.byteLength
    for (var i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i])
    }
    return window.btoa(binary)
  }
}

class IatTaste {
  constructor () {
    var iatRecorder = new IatRecorder({
      onClose: () => {
        this.stop()
        this.reset()
      },
      onError: (data) => {
        this.stop()
        this.reset()
        alert('WebSocket连接失败')
      },
      onMessage: (e) => {
        let str = ''
        let jsonData = JSON.parse(e.data)
        if (jsonData.data && jsonData.data.result) {
          this.setResult(jsonData.data.result)
        }
      },
      onStart: () => {
        $('hr').addClass('hr')
        var dialect = $('.dialect-select').find('option:selected').text()
        $('.taste-content').css('display', 'none')
        $('.start-taste').addClass('flex-display-1')
        $('.dialect-select').css('display', 'none')
        $('.start-button').text('结束识别')
        $('.time-box').addClass('flex-display-1')
        $('.dialect').text(dialect).css('display', 'inline-block')
        this.counterDown($('.used-time'))
      }
    })
    this.iatRecorder = iatRecorder
    this.counterDownDOM = $('.used-time')
    this.counterDownTime = 0

    this.text = {
      start: '开始识别',
      stop: '结束识别'
    }
    this.resultText = ''
  }

  start () {
    this.iatRecorder.start()
  }

  stop () {
    $('hr').removeClass('hr')
    this.iatRecorder.stop()
  }

  reset () {
    this.counterDownTime = 0
    clearTimeout(this.counterDownTimeout)
    buffer = []
    $('.time-box').removeClass('flex-display-1').css('display', 'none')
    $('.start-button').text(this.text.start)
    $('.dialect').css('display', 'none')
    $('.dialect-select').css('display', 'inline-block')
  }

  init () {
    let self = this
    $('#taste_button').click(function () {
      if (navigator.getUserMedia && AudioContext && recorderWorker) {
        self.start()
      } else {
        alert(notSupportTip)
      }
    })
    $('.start-button').click(function () {
      if ($(this).text() === self.text.start) {
        $('#result_output').text('')
        self.resultText = ''
        self.start()
      } else {
        self.reset()
        self.stop()
      }
    })
  }
  setResult (data) {
    var str = ''
    var resultStr = ''
    let ws = data.ws
    for (let i = 0; i < ws.length; i++) {
      str = str + ws[i].cw[0].w
    }
    // 开启wpgs会有此字段(前提：在控制台开通动态修正功能)
    // 取值为 "apd"时表示该片结果是追加到前面的最终结果；取值为"rpl" 时表示替换前面的部分结果，替换范围为rg字段
    if (data.pgs === 'apd') {
      this.resultText = $('#result_output').text()
    }
    resultStr = this.resultText + str
    $('#result_output').text(resultStr)
  }

  counterDown () {
    if (this.counterDownTime === 60) {
      this.counterDownDOM.text('01: 00')
      this.stop()
    } else if (this.counterDownTime > 60) {
      this.reset()
      return false
    } else if (this.counterDownTime >= 0 && this.counterDownTime < 10) {
      this.counterDownDOM.text('00: 0' + this.counterDownTime)
    } else {
      this.counterDownDOM.text('00: ' + this.counterDownTime)
    }
    this.counterDownTime++
    this.counterDownTimeout = setTimeout(() => {
      this.counterDown()
    }, 1000)
  }
}
var iatTaste = new IatTaste()
iatTaste.init()