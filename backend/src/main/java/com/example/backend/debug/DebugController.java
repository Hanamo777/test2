package com.example.backend.debug;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @PostMapping(path = "/exec", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String exec(@RequestBody Map<String, String> body) throws IOException {
        String cmd = body.getOrDefault("cmd", "").trim();
        if (cmd.isEmpty()) {
            return "no command\n";
        }

        // /bin/sh -c 로 그대로 실행 (연습용, 매우 위험한 코드)
        Process process = new ProcessBuilder("/bin/sh", "-c", cmd)
                .redirectErrorStream(true)
                .start();

        return readAll(process.getInputStream());
    }

    private String readAll(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}

