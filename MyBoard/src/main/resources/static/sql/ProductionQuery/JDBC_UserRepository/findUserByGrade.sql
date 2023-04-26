# Grade로 유저조회 특정 등급의 유저정보를 모두 선택함
SELECT *
FROM user
WHERE UserGrade = :grade;