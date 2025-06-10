package org.example.capstone_project.domain.food.controller;


import org.example.capstone_project.domain.food.PythonRun;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SimilarityController {

    @GetMapping("/similar")
    public String getSimilarFood(@RequestParam String target) {
        try {
            String resultJson = PythonRun.runPythonScript(target);

            JSONObject jsonObject = new JSONObject(resultJson);
            
            return jsonObject.getString("top3_names");

        } catch (Exception e) {
            e.printStackTrace();
            return "에러 발생: " + e.getMessage();
        }
    }
}
