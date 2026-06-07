
import logging

format = '%(asctime)s - %(name)s - %(levelname)s- %(filename)s:%(lineno)d - %(message)s'

logger = logging.getLogger('ai-service')
def init_logger():
    logger.setLevel(logging.DEBUG)

    if logger.hasHandlers():
        logger.handlers.clear()

    consoleHandler = logging.StreamHandler()
    consoleHandler.setLevel(logging.DEBUG)
    consoleHandler.setFormatter(logging.Formatter(format))
    logger.addHandler(consoleHandler)
