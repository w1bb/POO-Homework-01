package execution;

public enum ErrorType {
    NO_ERROR {
        @Override
        public String interpret() {
            return "No error";
        }
    },
    ERROR_BOARD_ROW_FULL {
        @Override
        public String interpret() {
            return "Cannot place card on table since row is full.";
        }
    }, // ? Ambiguous meaning - Cannot steal enemy card since the player's row is full.
    ERROR_INSUFFICIENT_MANA_FOR_HERO {
        @Override
        public String interpret() {
            return "Not enough mana to use hero's ability.";
        }
    },
    ERROR_NOT_ENEMY_ROW {
        @Override
        public String interpret() {
            return "Chosen row does not belong to the enemy.";
        }
    },
    ERROR_HERO_ALREADY_ATTACKED {
        @Override
        public String interpret() {
            return "Hero has already attacked this turn.";
        }
    },
    ERROR_SELECTED_ROW_NOT_ENEMY {
        @Override
        public String interpret() {
            return "Selected row does not belong to the enemy.";
        }
    },
    ERROR_SELECTED_ROW_NOT_ALLY {
        @Override
        public String interpret() {
            return "Selected row does not belong to the current player.";
        }
    },
    ERROR_ATTACKER_FROZEN {
        @Override
        public String interpret() {
            return "Attacker card is frozen.";
        }
    },
    ERROR_ATTACKER_ALREADY_ATTACKED {
        @Override
        public String interpret() {
            return "Attacker card has already attacked this turn.";
        }
    },
    ERROR_ATTACKER_NOT_ALLY{
        @Override
        public String interpret() {
            return "Attacked card does not belong to the current player.";
        }
    },
    ERROR_ATTACKED_CARD_NOT_ENEMY{
        @Override
        public String interpret() {
            return "Attacked card does not belong to the enemy.";
        }
    },
    ERROR_ATTACKED_CARD_NOT_TANK{
        @Override
        public String interpret() {
            return "Attacked card is not of type 'Tank'.";
        }
    },
    ERROR_PLACE_ENVIRONMENT_ON_BOARD {
        @Override
        public String interpret() {
            return "Cannot place environment card on table.";
        }
    },
    ERROR_INSUFFICIENT_MANA_TO_PLACE{
        @Override
        public String interpret() {
            return "Not enough mana to place card on table.";
        }
    },
    CRITICAL_HEART_HOUND_ABILITY_NO_CARD_TO_MOVE{
        @Override
        public String interpret() {
            return "??";
        }
    },
    CRITICAL_EMPRESS_THORINA_ATTACKS_NULL{
        @Override
        public String interpret() {
            return "CRITICAL: Empress Thorina has nothing to attack!";
        }
    },
    CRITICAL_LORD_ROYCE_FREEZES_NOTHING{
        @Override
        public String interpret() {
            return "CRITICAL: Lord Royce has nothing to freeze!";
        }
    },
    CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS{
        @Override
        public String interpret() {
            return "CRITICAL: Can only use attack on minion cards.";
        }
    },
    CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS_HEROS{
        @Override
        public String interpret() {
            return "CRITICAL: Can only use attack on minion/hero cards.";
        }
    },
    CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS{
        @Override
        public String interpret() {
            return "CRITICAL: Can only use ability on minion cards.";
        }
    },
    CRITICAL_BERSERKER_NO_ABILITY{
        @Override
        public String interpret() {
            return "CRITICAL: Berserker has NO ability implemented.";
        }
    },
    CRITICAL_GOLIATH_NO_ABILITY{
        @Override
        public String interpret() {
            return "CRITICAL: Goliath has NO ability implemented.";
        }
    },
    CRITICAL_SENTINEL_NO_ABILITY{
        @Override
        public String interpret() {
            return "CRITICAL: Sentinel has NO ability implemented.";
        }
    },
    CRITICAL_WARDEN_NO_ABILITY{
        @Override
        public String interpret() {
            return "CRITICAL: Warden has NO ability implemented.";
        }
    };

    public String interpret() {
        return "Undefined interpretation";
    }
}
