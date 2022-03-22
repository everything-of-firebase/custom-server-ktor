# Custom Server Ktor

**Ktor** 기반의 커스텀 서버를 이용하여 파이버베이스 프로젝트를 구성하는 샘플 코드입니다. 

---

## 셋업 방법

**NOTE**:We recommend to have an `your-username/your-username` repo on your personal GitHub account and not on your org (they're unsupported yet).

- [파이어베이스 콘솔](https://console.firebase.google.com)에 접속하여 신규 프로젝트를 생성한다. 

- **프로젝트 설정** -> **서비스 계정**으로 이동하여 **Firebase Admin SDK**를 선택한 뒤 **새 비공개 키 생성**을 클릭한다.

- 다운로드된 json 파일을 `google-service-account.json`으로 변경하고 프로젝트 소스의 `src/main/resources/secret` 하위 경로로 복사한다. 

- **IntelliJ**의 **Run Configuration**에서 다음과 같은 환경변수를 추가한다.

  ```
  GOOGLE_APPLICATION_CREDENTIALS=$PROJECT_DIR$/src/main/resources/secret/google-service-account.json
  ```
