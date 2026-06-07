
from pydantic import BaseModel, Field


class ChatParam(BaseModel):
    request: str=Field(..., description="用户输入")
    imageUrl: str=Field(..., description="图片地址")
    threadId: str=Field(..., description="会话ID")
