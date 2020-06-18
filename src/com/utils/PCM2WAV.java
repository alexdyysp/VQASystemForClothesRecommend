package com.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * PCM文件转换成Wav格式音频
 */
public class PCM2WAV {
    /**
     * 转换音频文件
     * @param src 需要转换的pcm音频路径
     * @param target 保存转换后wav格式的音频路径
     * @throws Exception
     */
    public static void convertAudioFiles(String src, String target) throws Exception {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);

        //计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 2;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 8000;
        header.BlockAlign = (short)(header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = PCMSize;

        byte[] h = header.getHeader();

        assert h.length == 44; //WAV标准，头部应该是44字节
        //write header
        fos.write(h, 0, h.length);
        //write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        System.out.println("Convert OK!");
    }

    //测试方法
    public static void main(String[] agrs) throws Exception{
        convertAudioFiles("D:\\DHU\\clothRec\\iat_ws_js_demo\\src\\resources\\tts\\helloaudio.pcm",
                "D:\\DHU\\clothRec\\iat_ws_js_demo\\src\\resources\\tts\\helloaudio.wav");
    }

}
