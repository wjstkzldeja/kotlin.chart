# base

## 베이스 기본 내용 변경 방법

#### 패키지명 변경

1. 왼쪽 project 탐색기 설정 클릭
2. Compact Middle Packages 선택 해제
3. 변경할 패키지명으로 Rename 진행
- https://overface.tistory.com/451

#### info.properties

- projectName
- applicationId
- apkHeader
- 해당 파일 이름 변경

#### strings.xml

- app_name
- 해당 파일 이름 변경

## 테마 변경

#### 하단 네비와 겹치는 이슈 해결

- DisplayUtils > windowSoftInputMode > setDecorFitsSystemWindows
- true = 오버랩 안되게, false = 오버랩

#### 풀스크린

- DisplayUtils > setFullScreenTheme 

#### 상태바

- 반투명 : 테마 > <item name="android:windowTranslucentStatus">true</item>
- 투명 : DisplayUtils > setFullScreenTheme

#### 네비

- 반투명 : 테마 > <item name="android:windowTranslucentNavigation">true</item>

#### 상태바, 하단 네비 투명

- DisplayUtils > setTransparentLightTheme

#### 투명,반투명,풀스크린 이슈

- adjustResize 동작 안함
- setDecorFitsSystemWindows=true를 적용해야 해서 하단 네비도 오버랩됨(레이아웃 맞추기 힘듬)
- 네비 높이 가져와서 강제로 레이아웃 맞춰야함