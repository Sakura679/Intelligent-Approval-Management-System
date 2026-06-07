1. 安装 uv
2. ai-service-python路径下执行 uv init 命令
3. 启动 uv run uvicorn app.main:app --reload --port 8000

**
本项目默认使用的是阿里百炼大模型
1. 请在 .env 文件中取消 OPENAI_API_KEY 注释并填入模型密钥
2. 若使用其他模型，请在完成 （1）后修改 ai-service-python\app\config\config.yaml 文件中 model: name以及base_url
**