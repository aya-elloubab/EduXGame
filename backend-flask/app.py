from flask import Flask, request, jsonify, render_template
import os
from llm_handler import get_prompt_result
from werkzeug.utils import secure_filename
from flask_cors import CORS

app = Flask(__name__)
app.secret_key = "secret_key"
app.config["UPLOAD_FOLDER"] = "/tmp"  # Changed to /tmp for Cloud Run
app.config["ALLOWED_EXTENSIONS"] = {"pdf"}
app.config["MAX_CONTENT_LENGTH"] = 16 * 1024 * 1024  # 16MB max file size
# Enable CORS for specific origin and route
CORS(app, resources={
    r"/query-pdf": {"origins": "http://localhost:3000"}
})

def allowed_file(filename):
    return '.' in filename and \
        filename.rsplit('.', 1)[1].lower() in app.config["ALLOWED_EXTENSIONS"]

@app.route("/", methods=["GET", "POST"])
def index():
    return render_template("index.html")

@app.route('/query-pdf', methods=['POST'])
def query_pdf():
    query = f"""Given the content of the PDF course material below, generate a JSON object that includes:
    - The chapter name.
    - A short description of the chapter.
    - A list of quiz questions with multiple answers, the correct answer(s), and an explanation for why each correct answer is correct.
    - A list of flip cards for key terms with their definitions or descriptions.
    - A list of matching elements.
    - Detailed short content summaries for key parts of the chapter, providing more information than just a single sentence. 
    All data in your response MUST be in the same language as the PDF content.

    The resulting JSON object must have the following structure:
    {{
        "chapterName": "Title of the Chapter",
        "description": "Short description of the chapter.",
        "quiz": [
            {{
                "question": "What is question 1?",
                "answers": ["answer1", "answer2", "answer3", "answer4"],
                "correctAnswer": ["answer2"],
                "explanation": "Explain here why 'answer2' is correct."
            }}
        ],
        "flipcards": [
            {{
                "front": "word1",
                "back": "definition or description or characteristic"
            }}
        ],
        "match": [
            {{
                "element": "element to match",
                "matchText": "correct match"
            }}
        ],
        "shortContent": [
            "A detailed short content entry about an important concept or section of the chapter.",
            "Another detailed short content entry, also providing more insights in the same PDF language."
        ]
    }} 

    IMPORTANT RESPONSE REQUIREMENTS:
    1. Provide ONLY the JSON object, with no additional text, comments, or markdown.
    2. Do not use any escape characters like \\n, \\t, or \\r.
    3. Use regular quotes (\"), not fancy quotes.
    4. Make sure all property names and string values are in double quotes.
    5. Ensure the JSON is valid and can be parsed by Python's json.loads().
    6. Do not include ```json or ``` markers.
    7. Format as a single line without line breaks."""
    return response_query_pdf(query)




@app.route('/quiz-query', methods=['POST'])
def query_quiz():
    quiz_query = f"""Given the content of the PDF course material below, generate a JSON object that includes only:
        - A list of quiz questions with multiple answers, the correct answer(s), and an explanation for why each correct answer is correct.
        All data in your response MUST be in the same language as the PDF content.

        The resulting JSON object must have the following structure:
        {{
            "quiz": [
                {{
                    "question": "What is question 1?",
                    "answers": ["answer1", "answer2", "answer3", "answer4"],
                    "correctAnswer": ["answer2"],
                    "explanation": "Explain here why 'answer2' is correct."
                }}
            ]
        }}

        IMPORTANT RESPONSE REQUIREMENTS:
        1. Provide ONLY the JSON object, with no additional text, comments, or markdown.
        2. Do not use any escape characters like \\n, \\t, or \\r.
        3. Use regular quotes (\") not fancy quotes.
        4. Make sure all property names and string values are in double quotes.
        5. Ensure the JSON is valid and can be parsed by Python's json.loads().
        6. Do not include ```json or ``` markers.
        7. Format as a single line without line breaks.
        """
    return response_query_pdf(quiz_query)

@app.route('/flipcards-query', methods=['POST'])
def query_flipcards():
    flipcards_query = f"""Given the content of the PDF course material below, generate a JSON object that includes only:
        - A list of flip cards for key terms, with each card having a 'front' (term) and 'back' (definition).
        All data in your response MUST be in the same language as the PDF content.

        The resulting JSON object must have the following structure:
        {{
            "flipcards": [
                {{
                    "front": "word1",
                    "back": "definition or description or characteristic"
                }}
            ]
        }}

        IMPORTANT RESPONSE REQUIREMENTS:
        1. Provide ONLY the JSON object, with no additional text, comments, or markdown.
        2. Do not use any escape characters like \\n, \\t, or \\r.
        3. Use regular quotes (\") not fancy quotes.
        4. Make sure all property names and string values are in double quotes.
        5. Ensure the JSON is valid and can be parsed by Python's json.loads().
        6. Do not include ```json or ``` markers.
        7. Format as a single line without line breaks.
        """
    return response_query_pdf(flipcards_query)


@app.route('/match-query-pdf', methods=['POST'])
def query_match():
    match_query = f"""Given the content of the PDF course material below, generate a JSON object that includes only:
        - A list of matching elements, where each item has 'element' and 'match' fields.
        All data in your response MUST be in the same language as the PDF content.

        The resulting JSON object must have the following structure:
        {{
            "match": [
                {{
                    "element": "element to match",
                    "matchText": "correct match"
                }}
            ]
        }}

        IMPORTANT RESPONSE REQUIREMENTS:
        1. Provide ONLY the JSON object, with no additional text, comments, or markdown.
        2. Do not use any escape characters like \\n, \\t, or \\r.
        3. Use regular quotes (\") not fancy quotes.
        4. Make sure all property names and string values are in double quotes.
        5. Ensure the JSON is valid and can be parsed by Python's json.loads().
        6. Do not include ```json or ``` markers.
        7. Format as a single line without line breaks.
        """
    return response_query_pdf(match_query)

@app.route('/short-content-query-pdf', methods=['POST'])
def query_shortcontent():
    shortcontent_query = f"""Given the content of the PDF course material below, generate a JSON object that includes only:
        - A list of detailed short content summaries for key parts of the chapter, each providing more information than a single sentence.
        All data in your response MUST be in the same language as the PDF content.

        The resulting JSON object must have the following structure:
        {{
            "shortContent": [
                "A detailed short content entry about an important concept or section of the chapter.",
                "Another detailed short content entry, also providing more insights in the same PDF language."
            ]
        }}

        IMPORTANT RESPONSE REQUIREMENTS:
        1. Provide ONLY the JSON object, with no additional text, comments, or markdown.
        2. Do not use any escape characters like \\n, \\t, or \\r.
        3. Use regular quotes (\") not fancy quotes.
        4. Make sure all property names and string values are in double quotes.
        5. Ensure the JSON is valid and can be parsed by Python's json.loads().
        6. Do not include ```json or ``` markers.
        7. Format as a single line without line breaks.
        """
    return response_query_pdf(shortcontent_query)

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy"}), 200

def response_query_pdf(query):
    try:
        # Retrieve file and query from request
        pdf_file = request.files.get('pdf')

        if not pdf_file :
            return jsonify({"error": "PDF file is required"}), 400

        if not allowed_file(pdf_file.filename):
            return jsonify({"error": "Invalid file type. Only PDF files are allowed."}), 400

        # Save file temporarily
        filename = secure_filename(pdf_file.filename)
        filepath = os.path.join(app.config["UPLOAD_FOLDER"], filename)
        pdf_file.save(filepath)

        try:
            # Process the PDF and query using the function
            result = get_prompt_result(filepath,query)         
            print("Type of result:", type(result))
            print("Raw result:", repr(result))
            # Clean up
            if os.path.exists(filepath):
                os.remove(filepath)
            print(result)

            return jsonify({
            "answer": result
            }), 200

        except Exception as e:
            # Clean up in case of error
            if os.path.exists(filepath):
                os.remove(filepath)
            raise e

    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    port = int(os.environ.get("PORT", 8080))
    app.run(host="0.0.0.0", port=port)