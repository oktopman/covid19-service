# covid19-service
코로나 확진자를 보기위해 타 사이트에 들어가지말고 내가 원하는정보만 알림을 통해 받아보고 싶어 만들게 되었습니다.
매일 업데이트 되는 코로나 확진자에 대한 정보를 슬랙 알림으로 받습니다.

#### 사용기술
- Springboot 2.3
- Mysql(rds) + Redis
- EC2 + Nginx + Travis CI + S3 + CodeDeploy
- Slack

#### 소개
공공데이터포털에서 제공해주는 OpenAPI(코로나바이러스감염증_시도발생_현황 조회 서비스)를 이용하여 데일리 환자 현황을 획득합니다.  
획득한 데이터는 rds 에 저장하고 당일 데이터는 redis를 통해 캐싱합니다.  
배치 시간은 공공데이터포털로 데이터를 요청했을때 당일 데이터 업데이트시간이 일정하지 않기때문에 매일 오전 12시로 설정 하였습니다.
지정한 채널에 Slack Webhook을 이용하여 시간마다 현황 알림을 받습니다.   
 
<img src="https://user-images.githubusercontent.com/55048593/95573567-9a80b400-0a66-11eb-8b99-77d0107c2bb5.png" width="600" height="400">
개발자 내부적으로 사용하는 API 외에 화면에서 사용하는 API는 2가지입니다.

- 데일리 지역별 확진자 현황 조회
- 최근 5일 지역별 환자 발생 현황 조회

API 정의서는 Spring Rest Docs 로 구현하였고 host:port/docs/index.html 에서 확인할 수 있습니다.

<img src="https://user-images.githubusercontent.com/55048593/95429408-51a4fe80-0985-11eb-9a79-1d9a917260c0.png" width="600" height="400">

화면은 추후 구현 예정입니다.


