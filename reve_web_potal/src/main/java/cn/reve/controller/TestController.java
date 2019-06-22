package cn.reve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {
    @GetMapping("/test")
    @ResponseBody
    public void test(){
        //context
        Context context = new Context();
        //ready to file
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", "who you are");
        context.setVariables(dataMap);
        File destFile = new File("d:/test.html");
        //general html
        try {
            PrintWriter printWriter = new PrintWriter(destFile);
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setTemplateMode(TemplateMode.HTML);
            resolver.setSuffix(".html");
            TemplateEngine engine = new TemplateEngine();
            engine.setTemplateResolver(resolver);
            engine.process("test", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
