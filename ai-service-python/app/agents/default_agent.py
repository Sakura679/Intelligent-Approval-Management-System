from langchain.agents import create_agent
from langchain_core.messages import AIMessage, BaseMessage
from langgraph.checkpoint.sqlite import SqliteSaver

from app.middleware.agent_middleware import summarizationMiddleware
from app.tools.agent_tool import approve, create_approval, get_approval_list, get_approve_list, get_local_datetime
from app.utils.beanFactory_handler import ChatModel, SqlSaverModel
from app.utils.logger_handler import logger
from app.utils.prompt_handler import load_sys_prompt


# 初始化 agent
checkpoint: SqliteSaver = SqlSaverModel.generator()
agent = create_agent(
    model=ChatModel.generator(),
    tools=[
        get_local_datetime,
        get_approval_list,
        get_approve_list,
        create_approval,
        approve
    ],
    system_prompt=load_sys_prompt(),
    middleware=[
        summarizationMiddleware()
    ],
    checkpointer=checkpoint,
)

def chat(request: str, imageUrl: str, threadId: str):
    content = [
        {
            'type': 'text',
            'text': request
        }
    ]

    if imageUrl:
        content.append({
            'type': 'image',
            'url': imageUrl
        })

    res = agent.invoke({
        'messages': [
            {
                'role': 'user',
                'content': content,
            }
        ]
    }, {
        'configurable': {
            'thread_id': threadId
        }
    })

    try:
        last_message = res['messages'][-1]
        if last_message.content:
            return last_message.content
    except Exception as e:
        logger.error(e)
        raise e
def get_history(threadId: str) -> list[BaseMessage]:
    configurable = {
        'configurable': {
            'thread_id': threadId
        }
    }
    
    try:
        res = checkpoint.get(configurable)
        if res and res['channel_values']:
            messages = res['channel_values']['messages']

            message_list = []
            for message in messages:
                if (message.type == 'ai' or message.type == 'human') and message.content:
                    message_list.append({
                        'type': message.type,
                        'content': message.content
                    })

            return message_list
        return []
    except Exception as e:
        logger.error(e)
        raise e

def clear_history(threadId: str):
    try:
        checkpoint.delete_thread(threadId)

        return 'success'
    except Exception as e:
        logger.error(e)
        raise e


