package org.example.capstone_project.domain.food.controller;


import org.example.capstone_project.domain.food.PythonRun;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SimilarityController {

    @GetMapping("/similar")       // 혹시 안되면 해보기, @GetMapping(value = "/similar", produces = "application/json; charset=UTF-8")
    public String getSimilarFood(@RequestParam String target) {
        try {
            String resultJson = PythonRun.runPythonScript(target);

            // 터미널에서 확인용
            System.out.println(">>> 받은 JSON:");
            System.out.println(resultJson);
            // 터미널에서 확인용

            JSONObject jsonObject = new JSONObject(resultJson);
            
            return jsonObject.getString("top3_names");   // "most_similar_food" == 코사인 유사도 max값 하나 뽑음.

        } catch (Exception e) {
            e.printStackTrace();
            return "에러 발생: " + e.getMessage();
        }
    }
}
