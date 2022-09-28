package com.margin.controller;

import java.io.IOException;
import java.text.DateFormat.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.margin.Serivce.FnoFileService;
import com.margin.entity.FnoFile;


@RestController
@RequestMapping("/fno")
public class FnoFileController {

	@Autowired
	FnoFileService fnoFileService;

	@PostMapping("/calculate")
	public ResponseEntity<FnoFile> processFnoBonus(@RequestBody FnoFile fno)
			throws IOException, IllegalArgumentException, IllegalAccessException {
		return new ResponseEntity<FnoFile>(fnoFileService.processFno(fno), HttpStatus.CREATED);

	}
	
	

}