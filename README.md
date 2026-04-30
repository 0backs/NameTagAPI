<div align=center>
<img src="https://capsule-render.vercel.app/api?type=waving&height=180&color=0:fc958d,100:a7f6fa&text=NameTagAPI&section=footer&reversal=false&textBg=false&fontColor=ffffff&fontSize=60"/>

#
플레이어의 이름표를 관리하는 플러그인입니다.
</div>

## 설치 방법
NameTagAPI.jar 플러그인을 plugins 폴더 안에 배포해 주세요.  
ProtocolLib.jar 플러그인을 plugins 폴더 안에 배포해 주세요. (필수적으로 필요한 플러그인이며, 없을 시 동작하지 않습니다.)

## 버전 안내
1.21.4 버전을 기준으로 제작되었으며, 특별한 능력이 없기에 상위 버전은 호환될 수도 있습니다.

## 플러그인 연동 방법
```java
NameTagAPI ntAPI = NameTagAPI.getInstance();
PlayerDatamanager pdManager = ntAPI.getPlayerDataManager();
pdManager.setTag(player, "tag");
```

## 스크립트 연동 방법(skript-reflect)
```java
import:
  org.Pursar.nameTagAPI.NameTagAPI

set {api} to NameTagAPI.getInstance()
set {manager} to {api}.getPlayerDataManager()
{mamager}.setTag(player, "tag")
```

## 사용
https://www.youtube.com/watch?v=fphf5_1d2Po
