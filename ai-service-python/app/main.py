
from dotenv import load_dotenv
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.api.v1 import chat_api
from app.utils.logger_handler import init_logger, logger

load_dotenv()
init_logger()

app = FastAPI()

# cors配置
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 注册路由
app.include_router(chat_api.router, prefix="/api/v1")

logger.info("api doc: http://127.0.0.1:8000/docs")


