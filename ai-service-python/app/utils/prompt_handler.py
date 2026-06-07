

import os
from app.utils.configfile_handler import load_default_config
from app.utils.path_handler import get_abs_path


config = load_default_config()

def load_sys_prompt():
    """加载系统提示语"""
    file_path = get_abs_path(config['prompt']['sys'])

    if not os.path.exists(file_path):
        raise Exception(f"{file_path} is not exists")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        return f.read()

