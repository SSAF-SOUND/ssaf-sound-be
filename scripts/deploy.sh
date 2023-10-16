# 가동중인 ssafsound-app 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=ssafsound-app" | grep -q . && docker stop ssafsound-app && docker rm ssafsound-app | true

# 기존 이미지 삭제
sudo docker rmi kimssafy/ssafsound-app:latest

# 도커허브 이미지 pull
sudo docker pull kimssafy/ssafsound-app:latest

# 도커 run
docker run -d -p 8080:8080 -e TZ=Asia/Seoul -v /home/ubuntu/logs:/logs --name ssafsound-app kimssafy/ssafsound-app:latest

docker rmi -f $(docker images -f "dangling=true" -q) || true