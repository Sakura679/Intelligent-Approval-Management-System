
from fastapi import APIRouter, Depends

from app.agents.default_agent import chat, clear_history, get_history
from app.core.security import verify_and_set_token
from app.schemas.chat_param import ChatParam


router = APIRouter()

@router.post("/agent/chat")
async def toChat(body: ChatParam, token: str=Depends(verify_and_set_token)):
    '''
    Docstring for toChat
    
    :param query: Description
    :type query: str
    :param fileUrl: Description
    :type fileUrl: str
    :param threadId: Description  like "thread_123"
    :type threadId: str
    :return: Description
    :rtype: str
    '''
    return chat(body.request, body.imageUrl, body.threadId)

@router.post("/agent/chat/history")
async def getChatHistory(body: ChatParam, token: str=Depends(verify_and_set_token)):
    """
    Docstring for getChatHistory
    
    :param body: Description
    :type body: ChatParam
    :param token: Description
    :type token: str
    """
    return get_history(body.threadId)

@router.post("/agent/chat/clear")
async def clearChatHistory(body: ChatParam, token: str=Depends(verify_and_set_token)):
    """
    Docstring for clearChatHistory
    
    :param body: Description
    :type body: ChatParam
    :param token: Description
    :type token: str
    """
    return clear_history(body.threadId)

