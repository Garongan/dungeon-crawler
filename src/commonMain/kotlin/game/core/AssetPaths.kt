package game.core

/**
 * Konstanta path untuk semua asset game.
 *
 * Semua referensi ke file resource harus menggunakan konstanta dari object ini,
 * tidak boleh hardcode string path secara langsung di kode lain.
 * Path bersifat relatif terhadap folder `resources/`.
 */
object AssetPaths {

    /** Path untuk sprite sheet karakter dan musuh */
    object Sprites {
        const val PLAYER_WARRIOR = "sprites/player_warrior.png"
        const val PLAYER_MAGE = "sprites/player_mage.png"
        const val PLAYER_ROGUE = "sprites/player_rogue.png"
        const val KNIGHT_IDLE = "sprites/player/warrior/knight_m_idle_anim_f0.png"

        const val ENEMY_SLIME = "sprites/enemy_slime.png"
        const val ENEMY_SKELETON = "sprites/enemy_skeleton.png"
        const val ENEMY_BAT = "sprites/enemy_bat.png"
        const val ENEMY_GHOST = "sprites/enemy_ghost.png"
        const val ENEMY_FIRE_ELEMENTAL = "sprites/enemy_fire_elemental.png"

        const val BOSS_GUARDIAN = "sprites/boss_guardian.png"
        const val BOSS_NECROMANCER = "sprites/boss_necromancer.png"
        const val BOSS_DRAGON = "sprites/boss_dragon.png"
        const val BOSS_VOID_LORD = "sprites/boss_void_lord.png"
    }

    /** Path untuk tileset per zona */
    object Tiles {
        const val ZONE1_RERUNTUHAN = "tiles/zone1_reruntuhan.png"
        const val ZONE2_KATAKOMBE = "tiles/zone2_katakombe.png"
        const val ZONE3_GUA_API = "tiles/zone3_gua_api.png"
        const val ZONE4_INTI_KEGELAPAN = "tiles/zone4_inti_kegelapan.png"
    }

    /** Path untuk file audio BGM dan SFX */
    object Audio {
        // Background Music
        const val BGM_HUB = "audio/bgm/bgm_hub.ogg"
        const val BGM_ZONE1 = "audio/bgm/bgm_zone1.ogg"
        const val BGM_ZONE2 = "audio/bgm/bgm_zone2.ogg"
        const val BGM_ZONE3 = "audio/bgm/bgm_zone3.ogg"
        const val BGM_ZONE4 = "audio/bgm/bgm_zone4.ogg"
        const val BGM_BOSS = "audio/bgm/bgm_boss.ogg"

        // Sound Effects
        const val SFX_SWORD_SWING = "audio/sfx/sfx_sword_swing.ogg"
        const val SFX_ARROW_SHOOT = "audio/sfx/sfx_arrow_shoot.ogg"
        const val SFX_HIT_IMPACT = "audio/sfx/sfx_hit_impact.ogg"
        const val SFX_DODGE = "audio/sfx/sfx_dodge.ogg"
        const val SFX_CHEST_OPEN = "audio/sfx/sfx_chest_open.ogg"
        const val SFX_ITEM_PICKUP = "audio/sfx/sfx_item_pickup.ogg"
        const val SFX_BOSS_ROAR = "audio/sfx/sfx_boss_roar.ogg"
        const val SFX_GAME_OVER = "audio/sfx/sfx_game_over.ogg"
    }

    /** Path untuk file data JSON (items, enemies, events, upgrades) */
    object Data {
        const val ITEMS = "data/items.json"
        const val ENEMIES = "data/enemies.json"
        const val EVENTS = "data/events.json"
        const val UPGRADES = "data/upgrades.json"
    }
}
