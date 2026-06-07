import os
import yaml

from app.utils.path_handler import get_abs_path

DEFAULT_CONFIG_FILE = 'config/config.yaml'

def load_default_config():
    abs_path = get_abs_path(DEFAULT_CONFIG_FILE)
    if not os.path.exists(abs_path):
        raise Exception(f"{abs_path} is not exists")
    
    return yaml.load(
        open(abs_path, 'r', encoding='utf-8'),
        Loader=yaml.FullLoader
    )
