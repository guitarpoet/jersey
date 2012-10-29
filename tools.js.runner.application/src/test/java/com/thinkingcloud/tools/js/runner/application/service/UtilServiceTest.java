package com.thinkingcloud.tools.js.runner.application.service;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class UtilServiceTest {

	private UtilService utils;

	@Before
	public void setup() {
		utils = new UtilService();
	}

	@Test
	public void testParseJsonData() throws IOException {
		String json = "{\"英文名称：\":\"[1S-(1alpha,2alpha,5alpha)]-6,6-dimethylspiro[bicyclo[3.1.1]heptane-2,2'-oxirane]\",\"英文别名：\":\"(1S-(1alpha,2beta,5alpha))-6,6-Dimethylspiro(bicyclo(3.1.1)heptane-2,2'-oxirane); (1S,2S,5R)-(+)-2,10-Epoxypinane; Pinane, 2,10-epoxy-, (1S,2S,5R)-(+)-; (1S,2S,5R)-6,6-dimethylspiro[bicyclo[3.1.1]heptane-2,2'-oxirane]; 6,6-dimethylspiro[bicyclo[3.1.1]heptane-2,2'-oxirane]\",\"CAS号：\":\"18680-30-3\",\"EINECS号：\":\"242-498-4\",\"分子式：\":\"C10H16O\",\"分子量：\":\"152.2334\",\"InChI：\":\"InChI=1/C10H16O/c1-9(2)7-3-4-10(6-11-10)8(9)5-7/h7-8H,3-6H2,1-2H3\",\"分子结构：\":\" \",\"密度：\":\"1.04g/cm3 \",\"沸点：\":\"202.1°C at 760 mmHg\",\"闪点：\":\"54.4°C\",\"蒸汽压：\":\"0.422mmHg at 25°C\",\"\":\"\"}";
		json = "{\"英文名称：\":\"N-[7-hydroxy-8-(2-hydroxy-5-nitro-phenyl)azo-1-naphthyl]acetamide\",\"英文别名：\":\"245-277-0; acetamide; N-{7-Hydr; LogP\",\"CAS号：\":\"22873-89-8\",\"EINECS号：\":\"245-277-0\",\"分子式：\":\"C18H14N4O5\",\"分子量：\":\"366.3276\",\"InChI：\":\"InChI=1/C18H14N4O5/c1-10(23)19-13-4-2-3-11-5-7-16(25)18(17(11)13)21-20-14-9-12(22(26)27)6-8-15(14)24/h2-9,24-25H,1H3,(H,19,23)\",\"分子结构：\":\" \",\"密度：\":\"1.48g/cm3 \",\"沸点：\":\"725.3°C at 760 mmHg\",\"闪点：\":\"392.4°C\",\"蒸汽压：\":\"9.7E-22mmHg at 25°C\",\"\":\"\"}";
		utils.parseJSONData(json);
		System.out.println(Arrays.toString(utils.getKeys(utils.parseJSONData(json))));
	}
}
