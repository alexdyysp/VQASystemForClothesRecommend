package com.graphSearch.controller;

import com.graphSearch.service.Manager_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller

public class ManagerController {
    @Autowired
    Manager_Service ManagerDao;



}
