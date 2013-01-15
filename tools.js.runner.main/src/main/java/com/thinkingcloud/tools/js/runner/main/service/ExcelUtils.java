package com.thinkingcloud.tools.js.runner.main.service;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.thinkingcloud.tools.js.runner.core.meta.Module;
import com.thinkingcloud.tools.js.runner.core.utils.BaseService;
import com.thinkingcloud.tools.js.runner.main.utils.ExcelIterator;

@Service("excel")
@Module(doc = "The utils for excel")
public class ExcelUtils extends BaseService {
	public ExcelIterator iterate(String file) throws IOException {
		return new ExcelIterator(new FileInputStream(file));
	}
}
