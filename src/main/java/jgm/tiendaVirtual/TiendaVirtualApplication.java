package jgm.tiendaVirtual;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TiendaVirtualApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendaVirtualApplication.class, args);
                
                Map<String, Object> props = new HashMap<>();
		props.put("server port", 8082);
	}

}
