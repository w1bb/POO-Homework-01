package execution;

/**
 * This data type represents a better way to interpret errors that could occur during the execution
 * of the game. All new errors should be implemented here and interpreted using the
 * <code>interpret()</code> method provided.
 */
public enum ErrorType {
    // No error at all
    NO_ERROR {
        @Override
        public String interpret() {
            return "No error";
        }
    },
    // Tested errors
    ERROR_BOARD_ROW_FULL {
        /**
         * {@inheritDoc}
         * An alternative interpretation could be: "Cannot steal enemy card since the player's row
         * is full" (see <code>ErrorType.ERROR_BOARD_ROW_FULL_ALT1</code>).
         */
        @Override
        public String interpret() {
            return "Cannot place card on table since row is full.";
        }
    },
    ERROR_BOARD_ROW_FULL_ALT1 {
        @Override
        public String interpret() {
            return "Cannot steal enemy card since the player's row is full.";
        }
    },
    ERROR_INSUFFICIENT_MANA_FOR_HERO {
        @Override
        public String interpret() {
            return "Not enough mana to use hero's ability.";
        }
    },
    ERROR_INSUFFICIENT_MANA_FOR_ENVIRONMENT {
        @Override
        public String interpret() {
            return "Not enough mana to use environment card.";
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
    ERROR_ATTACKER_NOT_ALLY {
        @Override
        public String interpret() {
            return "Attacked card does not belong to the current player.";
        }
    },
    ERROR_ATTACKED_CARD_NOT_ENEMY {
        @Override
        public String interpret() {
            return "Attacked card does not belong to the enemy.";
        }
    },
    ERROR_ATTACKED_CARD_NOT_TANK {
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
    ERROR_INSUFFICIENT_MANA_TO_PLACE {
        @Override
        public String interpret() {
            return "Not enough mana to place card on table.";
        }
    },
    ERROR_MISSING_CARD {
        @Override
        public String interpret() {
            return "No card available at that position.";
        }
    },
    ERROR_CHOSEN_CARD_NOT_ENVIRONMENT {
        @Override
        public String interpret() {
            return "Chosen card is not of type environment.";
        }
    },
    // Custom made errors
    CRITICAL_HEART_HOUND_ABILITY_NO_CARD_TO_MOVE {
        @Override
        public String interpret() {
            return "CRITICAL: Heart Hound has no card to move!";
        }
    },
    CRITICAL_EMPRESS_THORINA_ATTACKS_NULL {
        @Override
        public String interpret() {
            return "CRITICAL: Empress Thorina has nothing to attack!";
        }
    },
    CRITICAL_LORD_ROYCE_FREEZES_NOTHING {
        @Override
        public String interpret() {
            return "CRITICAL: Lord Royce has nothing to freeze!";
        }
    },
    CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS {
        @Override
        public String interpret() {
            return "CRITICAL: Can only use attack on minion cards!";
        }
    },
    CRITICAL_MINIONCARD_CAN_ONLY_ATTACK_MINIONS_HEROS {
        @Override
        public String interpret() {
            return "CRITICAL: Can only use attack on minion/hero cards!";
        }
    },
    CRITICAL_MINIONCARD_CAN_ONLY_ABILITY_MINIONS {
        @Override
        public String interpret() {
            return "CRITICAL: Can only use ability on minion cards!";
        }
    },
    CRITICAL_BERSERKER_NO_ABILITY {
        @Override
        public String interpret() {
            return "CRITICAL: Berserker has NO ability implemented!";
        }
    },
    CRITICAL_GOLIATH_NO_ABILITY {
        @Override
        public String interpret() {
            return "CRITICAL: Goliath has NO ability implemented!";
        }
    },
    CRITICAL_SENTINEL_NO_ABILITY {
        @Override
        public String interpret() {
            return "CRITICAL: Sentinel has NO ability implemented!";
        }
    },
    CRITICAL_WARDEN_NO_ABILITY {
        @Override
        public String interpret() {
            return "CRITICAL: Warden has NO ability implemented!";
        }
    },
    CRITICAL_CHECK_PLAYER_VALIDITY_WRONG_PLAYER {
        @Override
        public String interpret() {
            return "CRITICAL: Warden has NO ability implemented!";
        }
    },
    CRITICAL_PLACE_HERO_ON_BOARD {
        @Override
        public String interpret() {
            return "CRITICAL: Cannot place hero card on table!";
        }
    };

    /**
     * This method converts a given error name into a detailed explanation.
     * @return the detailed explanation
     */
    public String interpret() {
        return "Undefined interpretation";
    }
}
