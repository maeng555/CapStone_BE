import sys
import json
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from openai import OpenAI

import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

client = OpenAI(api_key="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")

def get_embedding(text, model="text-embedding-3-small"):
    return client.embeddings.create(input = [text], model=model).data[0].embedding

def similar_food(target):
    with open("/Users/maengjin-yeong/Documents/capstone_project/capstone_project/src/main/java/org/example/capstone_project/python/embedding/food_embeddings.json", "r", encoding="utf-8") as f:
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

    most_similar_food = max(similarities, key=similarities.get)
    similarity_list = sorted(similarities.items(), key=lambda x: x[1], reverse=True)[:3]

    result = {
        "most_similar_food": most_similar_food,
        "similarity": round(similarities[most_similar_food], 4),
        "top3": [{ "food": food, "similarity": round(sim, 4) } for food, sim in similarity_list],
        "top3_names": ", ".join([food for food, _ in similarity_list])
    }

    return result

if __name__ == "__main__":
    target_food = sys.argv[1]
    result = similar_food(target_food)
    print(json.dumps(result, ensure_ascii=False))