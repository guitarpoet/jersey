package info.thinkingcloud.jersey.main.service;

import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.utils.BaseService;
import info.thinkingcloud.jersey.main.utils.ExcelIterator;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;


@Service("excel")
@Module(doc = "The utils for excel")
public class ExcelUtils extends BaseService {
	public ExcelIterator iterate(String file) throws IOException {
		return new ExcelIterator(new FileInputStream(file));
	}
}
