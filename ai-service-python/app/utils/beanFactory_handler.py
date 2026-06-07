
from abc import ABC, abstractmethod
import os
import sqlite3
from typing import Optional
from langchain.chat_models import BaseChatModel, init_chat_model
from langchain_core.embeddings import Embeddings
from langgraph.checkpoint.base import BaseCheckpointSaver
from langchain_community.embeddings import DashScopeEmbeddings
from langgraph.checkpoint.sqlite import SqliteSaver

from app.utils.configfile_handler import load_default_config

config = load_default_config()

class ModelFactory(ABC):
    @abstractmethod
    def generator() -> Optional[BaseChatModel | Embeddings | BaseCheckpointSaver]:
        pass


class ChatModel(ModelFactory):
    def generator() -> Optional[BaseChatModel | Embeddings | BaseCheckpointSaver]:
        return init_chat_model(
            model=config['model']['name'],
            model_provider=config['model']['provider'],
            base_url=config['model']['base_url']
        )
    
class EmbeddingModel(ModelFactory):
    def generator() -> Optional[BaseChatModel | Embeddings | BaseCheckpointSaver]:
        return DashScopeEmbeddings(model=config['embedding']['model'])
    
class SqlSaverModel(ModelFactory):
    def generator() -> Optional[BaseChatModel | Embeddings | BaseCheckpointSaver]:
        db_path = config['agent']['checkpoint']
        if not os.path.exists(os.path.dirname(db_path)):
            os.makedirs(os.path.dirname(db_path))

        conn = sqlite3.connect(db_path, check_same_thread=False)
        return SqliteSaver(conn)