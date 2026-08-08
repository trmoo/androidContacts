# 연락처 관리 앱 (Contacts Manager)

SQLite에 연락처를 저장·조회·수정·삭제하고 바로 전화를 걸 수 있는 안드로이드 애플리케이션입니다.
모바일소프트웨어 과제(`homework04`)로 작성했으며, 이 디렉터리는 **ver10 (완성)** 버전의 소스 백업입니다.

- 패키지: `com.example.homework04`
- 언어: Java
- 저장소: SQLite (`SQLiteOpenHelper`)
- UI: `ListView` + 커스텀 `BaseAdapter`
- 지원 라이브러리: `android.support.v7` (AppCompat, AndroidX 이전 세대)

---

## 화면 구성

```
┌──────────────────────────────────────────────┐
│ [이름]  [별명]  [전화번호]            [콜]   │  ← 입력 영역
├──────────────────────────────────────────────┤
│ [전체보기]     [이름검색]     [초기화]       │  ← 조회 영역
├──────────────────────────────────────────────┤
│ [추가]          [수정]         [삭제]        │  ← 편집 영역
├──────────────────────────────────────────────┤
│  이름(*)   |   별명   |    전화번호(*)       │  ← 헤더
├──────────────────────────────────────────────┤
│  홍길동    |  길동이  |   010-1234-5678      │
│  김철수    |          |   010-2222-3333      │  ← ListView
│                    ⋮                         │
└──────────────────────────────────────────────┘
```

`(*)` 표시된 **이름**과 **전화번호**는 필수 입력 항목이고, **별명**은 선택 항목입니다.

## 기능

| 버튼 | ID | 동작 |
|------|----|------|
| 콜 | `dial` | 전화번호 입력값으로 `ACTION_DIAL` 인텐트를 실행해 다이얼러를 띄우고 앱을 종료합니다. |
| 전체보기 | `showAll` | DB의 모든 연락처를 이름·별명 순으로 정렬해 목록에 표시합니다. |
| 이름검색 | `search` | 이름이 **정확히 일치**하는 항목만 목록에 남깁니다. 결과가 없으면 토스트로 알립니다. |
| 초기화 | `erase` | 입력 필드 3개를 모두 비우고 버튼 상태를 리셋합니다. |
| 추가 | `add` | 새 연락처를 DB에 INSERT합니다. |
| 수정 | `update` | 목록에서 선택한 항목을 입력값으로 UPDATE합니다. |
| 삭제 | `delete` | 확인 다이얼로그(`네` / `아니오`)를 거친 뒤 선택 항목을 DELETE합니다. |

### 그 외 동작

- **앱 실행 시 자동 로딩** — `onCreate()`에서 `getAllData()`를 호출해 저장된 연락처를 바로 보여줍니다.
- **전화번호 자동 서식** — `PhoneNumberFormattingTextWatcher("KR")`가 입력 중인 번호를 한국 형식(`010-1234-5678`)으로 자동 정리합니다.
- **목록 항목 선택** — 항목을 탭하면 이름·별명·전화번호가 입력 필드로 채워지고, 수정·삭제가 가능한 상태가 됩니다.
- **중복 방지** — 이름·별명·전화번호가 **모두** 같은 항목은 추가·수정되지 않고 토스트로 안내합니다.
- **소프트 키보드 자동 숨김** — 버튼을 누르면 키보드가 내려갑니다.

### 버튼 활성화 규칙

세 입력 필드에 각각 `TextWatcher`를 달아 입력 상태에 따라 버튼을 실시간으로 켜고 끕니다. 잘못된 조작 자체가 불가능하도록 만든 부분입니다.

| 버튼 | 활성화 조건 |
|------|-------------|
| 이름검색 | 이름이 비어 있지 않을 때 |
| 초기화 | 세 필드 중 하나라도 값이 있을 때 |
| 추가 | 이름과 전화번호가 모두 채워졌을 때 |
| 수정 | 목록에서 항목을 선택한 상태(`position != INVALID_POSITION`)에서 이름·전화번호가 채워졌을 때 |
| 삭제 | 목록에서 항목을 선택했고, 입력값이 그 항목과 아직 동일할 때 |

선택한 항목의 내용을 한 글자라도 고치면 삭제가 꺼지고 추가가 켜집니다. "선택한 것을 지우는 동작"과 "고친 내용을 새로 넣는 동작"을 구분하기 위한 처리입니다.

## 데이터베이스

`MyDBHelper`가 관리합니다.

- 파일명: `myContacts1.db`
- 버전: `1`

```sql
CREATE TABLE people (
    _id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name  TEXT,
    alias TEXT,
    phone TEXT
);
```

조회는 항상 `ORDER BY name, alias`로 정렬합니다. `onUpgrade()`는 테이블을 DROP 후 재생성합니다(기존 데이터 삭제).

## 파일 구성

이 백업에는 직접 작성한 파일만 들어 있습니다. 안드로이드 프로젝트에 넣을 때의 경로는 다음과 같습니다.

| 파일 | 프로젝트 내 경로 | 설명 |
|------|------------------|------|
| `MainActivity.java` | `app/src/main/java/com/example/homework04/` | 화면 제어, 버튼 처리, DB CRUD 전반 |
| `MyDBHelper.java` | `app/src/main/java/com/example/homework04/` | `SQLiteOpenHelper` 구현, 테이블 생성 |
| `MyData.java` | `app/src/main/java/com/example/homework04/` | 연락처 한 건을 담는 모델 (이름/별명/전화번호) |
| `MyAdapter.java` | `app/src/main/java/com/example/homework04/` | `BaseAdapter` 구현, `row.xml`을 inflate |
| `activity_main.xml` | `app/src/main/res/layout/` | 메인 화면 레이아웃 |
| `row.xml` | `app/src/main/res/layout/` | 리스트 한 줄 레이아웃 |
| `styles.xml` | `app/src/main/res/values/` | `AppTheme`, 글자색 지정용 `MyStyle` |
| `back.png` | `app/src/main/res/drawable/` | 배경 이미지 (`@drawable/back`) |

### 프로젝트에 적용하기

1. Android Studio에서 패키지명 `com.example.homework04`으로 새 프로젝트를 만듭니다.
2. 위 표의 경로대로 파일을 복사합니다.
3. `AndroidManifest.xml`에 `MainActivity`를 런처 액티비티로 등록하고, `application` 태그에 `android:theme="@style/AppTheme"`를 지정합니다.
4. `res/values/colors.xml`에 `colorPrimary`, `colorPrimaryDark`, `colorAccent`를 정의합니다 (`styles.xml`이 참조).
5. 지원 라이브러리 의존성을 추가합니다.
   ```gradle
   implementation 'com.android.support:appcompat-v7:28.0.0'
   ```

레이아웃의 버튼들은 `android:onClick="onClick"`으로 `MainActivity.onClick(View)` 하나에 모두 연결되어 있으므로, 별도의 리스너 등록 코드는 필요 없습니다.

전화 걸기는 `ACTION_DIAL`(다이얼러를 열어주기만 함)을 사용하므로 `CALL_PHONE` 권한이 필요 없습니다.

## 알려진 제약

과제 제출본 그대로이며, 다음 사항은 의도적으로 단순화되었거나 개선의 여지가 있는 부분입니다.

- **검색은 이름 완전 일치만 지원**합니다. 부분 일치나 별명·번호 검색은 되지 않습니다.
- **수정·삭제 시 `_id`가 아니라 (이름, 별명, 전화번호) 조합으로 행을 찾습니다.** 추가 단계에서 완전 중복을 막고 있어 실사용에는 문제가 없지만, `_id`를 쓰는 편이 더 안전합니다.
- **검색 결과가 없을 때** 내부 리스트는 비워지지만 `notifyDataSetChanged()`가 호출되지 않아, 토스트만 뜨고 화면에는 직전 목록이 남아 있습니다.
- **DB 접근이 UI 스레드에서** 이루어집니다. 데이터 양이 적은 과제 규모라 체감 문제는 없습니다.
- `android.support.v7` 기반이므로, 현재 버전의 Android Studio에서 빌드하려면 AndroidX로 마이그레이션이 필요할 수 있습니다.

## 개발 환경

- Android Studio (2018년 기준)
- Java, Android Support Library v7 (`Theme.AppCompat.Light.DarkActionBar`)
- 최종 수정: 2018년 12월

## 저작권

Copyright © 2018 (작성자명). All rights reserved.

이 프로젝트는 대학 **모바일소프트웨어** 교과목의 개인 과제로 작성된 학습용 결과물입니다.
소스 코드의 저작권은 작성자에게 있으며, 별도의 오픈소스 라이선스를 적용하지 않습니다.

- **열람·학습 목적의 참고는 자유**롭게 하셔도 됩니다.
- **무단 복제·재배포·상업적 이용은 허용하지 않습니다.**
- 이 코드를 **그대로 또는 일부만 수정해 자신의 과제로 제출하는 행위는 표절**에 해당하며, 그로 인해 발생하는 학사상 불이익에 대해 작성자는 책임지지 않습니다.
- 코드를 참고해 작성한 결과물을 공개할 경우 출처를 밝혀 주시기 바랍니다.

### 제3자 리소스
- Android SDK 및 Android Support Library는 Google LLC의 저작물이며 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)을 따릅니다.

### 면책

이 소프트웨어는 학습 목적으로 "있는 그대로(AS IS)" 제공되며, 특정 목적에의 적합성을 포함한 어떠한 명시적·묵시적 보증도 하지 않습니다. 사용으로 인해 발생하는 데이터 손실 등의 문제에 대해 작성자는 책임을 지지 않습니다.
