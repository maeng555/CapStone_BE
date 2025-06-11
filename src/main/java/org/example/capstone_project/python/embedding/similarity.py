import sys
import json
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from openai import OpenAI
import os

client = OpenAI(api_key="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")  # 그대로 둬도 됨

def get_embedding(text, model="text-embedding-3-small"):
    return client.embeddings.create(input = [text], model=model).data[0].embedding

def similar_food(target):
    json_path = os.getenv("FOOD_EMBEDDING_JSON")
    if not json_path:
        raise ValueError("환경변수 'FOOD_EMBEDDING_JSON'이 설정되지 않았습니다.")

    with open(json_path, "r", encoding="utf-8") as f:
        loaded_dict = json.load(f)

    if target in loaded_dict:
        target_vector = np.array(loaded_dict[target]).reshape(1, -1)
    else:
        target_vector = np.array(get_embedding(target)).reshape(1, -1)

    similarities = {}
    for food, vector in loaded_dict.items():
        if food == target:
            continue
        vector = np.array(vector).reshape(1, -1)
        similarity = cosine_similarity(target_vector, vector)[0][0]
        similarities[food] = similarity

    similarity_list = sorted(similarities.items(), key=lambda x: x[1], reverse=True)[:3]

    result = {
        "most_similar_food": similarity_list[0][0],
        "similarity": round(similarity_list[0][1], 4),
        "top3": [{ "food": food, "similarity": round(sim, 4) } for food, sim in similarity_list],
        "top3_names": ", ".join([food for food, _ in similarity_list])
    }

    return result

if __name__ == "__main__":
    target_food = sys.argv[1]
    result = similar_food(target_food)
    print(json.dumps(result, ensure_ascii=False))