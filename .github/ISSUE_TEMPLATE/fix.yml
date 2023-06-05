name: fix
description: 버그 수정
title: '[FIX]: '
labels: [fix]
assignees: []
body:
  - type: textarea
    attributes:
      label: 관련 이슈
      description: |
        (필수) 관련된 버그가 소개된 이슈의 번호를 작성해주세요.
        이슈번호 앞에 `- #`을 꼭 붙여주세요. 아래 에디터에서 `- #`까지 입력하면 자동완성 도구가 나타납니다.

        예:
          - #1
      placeholder: |
        - #1
    validations:
      required: true
  - type: dropdown
    id: fix_info
    attributes:
      label: 버그 수정 범위
      description: |
        (필수) 버그 수정 범위를 선택해주세요.
      options:
        - 전부 고침
        - 일부만 고침
    validations:
      required: true
  - type: textarea
    attributes:
      label: 주의사항
      description: |
        (옵션) 특별히 주의해야할 내용이 있다면 알려주세요.

        예시:
          - 버그 상황이 부분적으로만 재현되어, ~~ 로직만 선 반영합니다.
    validations:
      required: false
