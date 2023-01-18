package org.entando.bundle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.io.File;

@SpringBootTest
class UserServiceApplicationTests {

	@MockBean
	JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {

		String tempDir = System.getProperty(PROP_TMP_DIR);
		System.out.println(">>> " + tempDir);

		File file = new File("/home/matteo/Pictures/WTF.png");
		System.out.println(">>> " + file.getName());
		System.out.println(">>> " + file.getAbsolutePath());
	}

	final static String PROP_TMP_DIR = "java.io.tmpdir";
}
