
from langchain.agents.middleware import SummarizationMiddleware

from app.utils.beanFactory_handler import ChatModel

def summarizationMiddleware():
    return SummarizationMiddleware(
        model=ChatModel.generator(),
        trigger=('tokens', 4000),
        keep=('messages', 10),
    )

