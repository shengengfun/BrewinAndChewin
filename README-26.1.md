# Brewin' And Chewin' — NeoForge 26.1 移植分支

<img src="https://img.shields.io/badge/Minecraft-26.1.2-brightgreen" alt="Minecraft 26.1.2">
<img src="https://img.shields.io/badge/NeoForge-26.1.2.114-orange" alt="NeoForge 26.1.2.114">
<img src="https://img.shields.io/badge/Version-5.0.0--ex261-blue" alt="Version 5.0.0-ex261">
<img src="https://img.shields.io/badge/Java-25-red" alt="Java 25">
<img src="https://img.shields.io/badge/License-MIT-black" alt="License MIT">

Brewin' And Chewin' 是 Farmer's Delight 的发酵/酿酒扩展。本仓库是它面向
**Minecraft 26.1.2 / NeoForge 26.1.2.114** 的移植分支，分支名 `26.1`
（上游为 `MerchantPug/BrewinAndChewin`，默认分支 `1.21.1`）。

> **状态：移植进行中，尚未编译通过。** 详细进度、已确认的 26.1 API 结论与剩余工作见
> `modpack-update/reports/bac-26.1-status.md`（在 LostMelody 工作区内）。

## 环境要求

| 项目 | 版本 |
| --- | --- |
| Minecraft | 26.1.2 |
| NeoForge | 26.1.2.114 |
| JDK | 25 |
| Gradle | 由 ModDevGradle 2.0.148 驱动（本机用 `modpack-update/tools/gradle-9.2.1`） |
| 前置模组 | Farmer's Delight（26.1 社区移植版，`farmersdelight`） |

## 仓库结构变化

上游是多加载器结构（`common/` + `fabric/` + `neoforge/` + Kotlin `buildSrc`）。26.1 只做
NeoForge，所以：

- `common/` 与 `neoforge/` 已合并成单一源码集（`src/main/java`、`src/main/resources`、
  `src/generated/resources`），两者包名本来就不冲突；
- `fabric/` 模块、`buildSrc/`、`.kts` 构建脚本与 Gradle wrapper 已删除。

## 快速开始

Farmer's Delight 与 AppleSkin 在 26.1 上还没有公开 maven，所以两个 jar 按**普通文件依赖**
引入（26.1 已不混淆，不需要 deobf）。请先按 `libs/README.md` 把 jar 放进 `libs/`：

```
libs/FarmersDelight-26.1.2-1.3.3-fix4.jar
libs/appleskin-neoforge-mc26.1-3.0.9.jar
```

然后构建（Windows）：

```powershell
$env:JAVA_HOME = 'D:\Project\LostMelody\modpack-update\tools\jdk25\jdk-25.0.4.1+1'
D:\Project\LostMelody\modpack-update\tools\gradle-9.2.1\bin\gradle.bat build --console=plain
```

`-Dorg.gradle.java.installations.paths=…jdk21` 是必须的：NeoForge Runtime Tooling 需要 JDK 21，
不指定的话 Gradle 会去 foojay 下载。构建产物在 `build/libs/`。

## 26.1 移植要点（本分支已落地的部分）

| 1.21.1 | 26.1 |
| --- | --- |
| `ResourceLocation` | `net.minecraft.resources.Identifier` |
| `ItemInteractionResult` / `InteractionResultHolder` | `InteractionResult`（sealed interface） |
| `MobEffects.DAMAGE_RESISTANCE` 等 | `RESISTANCE` / `SPEED` / `HASTE` / `SLOWNESS` / `NAUSEA` |
| `UseAnim` | `ItemUseAnimation` |
| `DirectionProperty` | `EnumProperty<Direction>` |
| `BlockAndTintGetter`（`world.level`） | `client.renderer.block.BlockAndTintGetter` |
| `BakedQuad`（`client.renderer.block.model`） | `client.resources.model.geometry.BakedQuad`（record） |
| `ModelData` / `ModelProperty` | `neoforge.model.data` |
| `advancements.critereon` | `advancements.criterion` |
| `Item.Properties#tag(TagKey)` | 已删除，标签改为数据包 JSON |
| `FoodProperties.Builder#effect(...)` / `#fast()` | 效果移到 `Consumable` 的 `ConsumeEffect`；`fast()` 变成 `ConsumeEffect` 之外的 `Consumable#consumeSeconds(0.8F)` |
| `LootItemFunctionType` / `LootItemConditionType` | 已删除，类型注册表直接收 `MapCodec`，由 `codec()` 暴露 |
| `LootContext#getParamOrNull` | `getOptionalParameter` |
| `LootContextParam` / `LootContextParamSet` | `ContextKey` / `ContextKeySet` |
| GreenhouseConfig | NeoForge `ModConfigSpec`（见下） |
| 模组入口 `(IEventBus)` | `(ModContainer container)` → `container.getEventBus()` |

## 已记录的降级与删除

1. **EMI 联动整体删除**（`integration/emi`、两个 `EMIFill*` 网络包）：EMI 没有 26.1 构建。
2. **Create 联动删除**（`BnCCreateDelegate` 及 `data/create` 下的灌注配方）：Create 最高只到 1.21.1。
3. **GreenhouseConfig 换成 NeoForge `ModConfigSpec`**：配置文件键名保持不变，但 COMMON 配置不再
   由服务端同步给客户端（NeoForge 的 COMMON 配置是「各读各的」），Tipsy 文本错乱阈值因此在
   多人游戏中取客户端自己的值。
4. **Fabric 模块删除**：本分支只面向 NeoForge。
5. AppleSkin 联动暂时保留（用一个客户端 mixin）。

## 许可与致谢

- **许可**：MIT（上游 `Properties.kt` 声明）。
- **致谢**：原作者 Probleyes / Umpaz / MerchantCalico；26.1 分支由 LostMelody 维护。
