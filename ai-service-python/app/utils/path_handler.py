import os

def get_root_path():
    return os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

def get_abs_path(path: str):
    """
    Docstring for get_abs_path
    
    :param path: example a/b/c.txt
    :type path: str
    """
    return os.path.join(get_root_path(), path)


if __name__ == '__main__':
    print(get_root_path())
    print(get_abs_path('config/config.yaml'))