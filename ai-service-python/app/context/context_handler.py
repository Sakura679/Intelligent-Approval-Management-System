

import contextvars


token_context = contextvars.ContextVar("user_token", default=None)


def set_user_token(token: str):
    token_context.set(token)

def get_user_token() -> str:
    token = token_context.get()

    if not token:
        raise ValueError("No user token set")

    return token



