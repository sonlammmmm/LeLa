import os
import re
import json

base_dir = r"d:\LeLa\src\main\java\com\lela"

def extract_path(annotation_value):
    # Extracts path from annotations like @GetMapping("/path"), @GetMapping(value = "/path")
    match = re.search(r'["\']([^"\']+)["\']', annotation_value)
    if match:
        return match.group(1)
    return ""

def process_java_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Check if it's a controller
    if '@RestController' not in content and '@Controller' not in content:
        return None

    # Get class name
    class_match = re.search(r'class\s+(\w+Controller)\b', content)
    if not class_match:
        return None
    class_name = class_match.group(1)
    
    # Get class-level RequestMapping
    base_path = ""
    class_mapping_match = re.search(r'@RequestMapping\s*\(\s*(.*?)\s*\)', content)
    if class_mapping_match:
        base_path = extract_path(class_mapping_match.group(1))

    # Strip the class level annotations so we don't confuse them with method annotations
    # Actually, let's just find all method annotations
    # Pattern to match @XxxMapping(...) followed by method signature
    method_pattern = re.compile(r'@(GetMapping|PostMapping|PutMapping|DeleteMapping|PatchMapping|RequestMapping)\s*(?:\(\s*(.*?)\s*\))?[\s\S]*?(?:public|protected|private)\s+[\w<>,\[\]\s]+\s+(\w+)\s*\(')
    
    methods = method_pattern.findall(content)
    
    requests = []
    for mapping_type, mapping_val, method_name in methods:
        method = mapping_type.replace('Mapping', '').upper()
        if method == 'REQUEST':
            method = 'GET' # fallback
            
        path = extract_path(mapping_val) if mapping_val else ""
        
        full_path = f"{base_path}{path}".replace('//', '/')
        if not full_path.startswith('/'):
            full_path = '/' + full_path
            
        # Basic parsing of path variables to postman format if needed, but Postman accepts /api/tags/{id}
        # Postman prefers {{baseUrl}}/path/to/{{id}} or :id for path variables. We'll leave as {id} for now, it's fine.
        
        requests.append({
            "name": method_name,
            "method": method,
            "url": full_path
        })
        
    if not requests:
        return None
        
    return {
        "folder_name": class_name,
        "requests": requests
    }

collection = {
    "info": {
        "name": "LeLa Complete API Collection",
        "description": "Auto-generated collection for all controllers in the project",
        "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    },
    "item": []
}

for root, dirs, files in os.walk(base_dir):
    for file in files:
        if file.endswith("Controller.java"):
            filepath = os.path.join(root, file)
            controller_data = process_java_file(filepath)
            if controller_data:
                folder_item = {
                    "name": controller_data["folder_name"],
                    "item": []
                }
                
                for req in controller_data["requests"]:
                    
                    # Add base URL setup and path components
                    url_parts = req["url"].strip('/').split('/')
                    
                    postman_request = {
                        "name": req["name"],
                        "request": {
                            "method": req["method"],
                            "header": [
                                { "key": "Content-Type", "value": "application/json" }
                            ],
                            "url": {
                                "raw": "http://localhost:8080" + req["url"],
                                "protocol": "http",
                                "host": ["localhost"],
                                "port": "8080",
                                "path": url_parts
                            }
                        }
                    }
                    
                    # Add simple test script
                    postman_request["event"] = [
                        {
                            "listen": "test",
                            "script": {
                                "exec": [
                                    "pm.test(\"Status code is successful\", function () {",
                                    "    pm.expect(pm.response.code).to.be.oneOf([200, 201, 204]);",
                                    "});"
                                ],
                                "type": "text/javascript"
                            }
                        }
                    ]
                    
                    # Try to guess body if it's POST/PUT
                    if req["method"] in ["POST", "PUT", "PATCH"]:
                        postman_request["request"]["body"] = {
                            "mode": "raw",
                            "raw": "{\n    \n}"
                        }
                        
                    folder_item["item"].append(postman_request)
                    
                collection["item"].append(folder_item)

with open(r"d:\LeLa\LeLa_Full_Postman_Collection.json", "w", encoding="utf-8") as out:
    json.dump(collection, out, indent=2, ensure_ascii=False)

print("Generated full postman collection at LeLa_Full_Postman_Collection.json")
