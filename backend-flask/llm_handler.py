from PyPDF2 import PdfReader
from langchain.chains import RetrievalQA
from langchain.text_splitter import CharacterTextSplitter
from langchain_openai  import OpenAIEmbeddings
from langchain_community.vectorstores import FAISS
from langchain_community.llms import OpenAI
from langchain_community.chat_models import ChatOpenAI
import json
from flask import  jsonify
import os
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

def extract_text_from_pdf(pdf_path):
    """Extract text from PDF file."""
    reader = PdfReader(pdf_path)
    text = ""
    for page in reader.pages:
        text += page.extract_text()
    return text

def create_qa_chain(text):
    """Create a QA chain with the processed text."""
    # Split text into chunks
    text_splitter = CharacterTextSplitter(
        separator="\n",
        chunk_size=1000,
        chunk_overlap=200,
        length_function=len
    )
    texts = text_splitter.split_text(text)
    
        
    api_key = os.getenv("OPENAI_API_KEY")
    if not api_key:
        raise Exception("OpenAI API key is missing. Please set the OPENAI_API_KEY environment variable.")

    # Create embeddings and vector store
    embeddings = OpenAIEmbeddings(
        openai_api_key=os.getenv("OPENAI_API_KEY")
    )
    vectorstore = FAISS.from_texts(texts, embeddings)
    llm = ChatOpenAI(
        model_name="gpt-4-0125-preview",  # Using GPT-4 Turbo
        temperature=0.7,
        max_tokens=4000,
        openai_api_key=os.getenv("OPENAI_API_KEY")
    )
    
    # Create QA chain
    qa_chain = RetrievalQA.from_chain_type(
        llm=llm,
        chain_type="stuff",
        retriever=vectorstore.as_retriever(),
        return_source_documents=False
    )
    # Create QA chain with higher temperature for creative responses
    
    
    return qa_chain

def get_prompt_result(pdf_path,query):
    """Process PDF and return answer to query."""
    try:
        # Extract text from PDF
        text = extract_text_from_pdf(pdf_path)
        
        # Create QA chain
        try:
            qa_chain = create_qa_chain(text)
        except Exception as chain_error:
            raise Exception(f"Error creating QA chain: {str(chain_error)}")        
        # If no specific query is provided, use the course material prompt
        
        # Get response
        response = qa_chain({"query": query})
        parsed_result = response['result']
        print(parsed_result)
        return json.loads(parsed_result)
     
    except Exception as e:
        raise Exception(f"Error processing query: {str(e)}")