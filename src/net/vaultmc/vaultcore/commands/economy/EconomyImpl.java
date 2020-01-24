/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.vaultmc.vaultcore.commands.economy;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class EconomyImpl implements Economy {
    @Getter
    private static EconomyImpl instance;

    public EconomyImpl() {
        /*
        if (instance != null) {
            throw new InstantiationError("Cannot instantiate multiple instances");
        }
         */

        instance = this;
    }

    private static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "VaultEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return "$" + VaultCore.numberFormat.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "dollars";
    }

    @Override
    public String currencyNameSingular() {
        return "dollar";
    }

    @Override
    @Deprecated
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    @Override
    @Deprecated
    public double getBalance(String playerName) {
        return getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return -1;
    }

    @Override
    @Deprecated
    public double getBalance(String playerName, String world) {
        return getBalance(Bukkit.getOfflinePlayer(playerName), world);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return VLOfflinePlayer.getOfflinePlayer(player).getDataConfig().getDouble("economy." + world, 0);
    }

    @Override
    @Deprecated
    public boolean has(String playerName, double amount) {
        throw new UnsupportedOperationException("Must specify world");
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        throw new UnsupportedOperationException("Must specify world");
    }

    @Override
    @Deprecated
    public boolean has(String playerName, String worldName, double amount) {
        return has(Bukkit.getOfflinePlayer(playerName), worldName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return getBalance(player, worldName) >= amount;
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.FAILURE, "Must specify world");
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), worldName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        FileConfiguration data = VLOfflinePlayer.getOfflinePlayer(player).getDataConfig();
        if (data.contains("economy." + worldName)) {
            data.set("economy." + worldName, round(
                    data.getDouble("economy." + worldName) - amount));
        } else {
            data.set("economy." + worldName, round(-amount));
        }
        VLOfflinePlayer.getOfflinePlayer(player).saveData();
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Successfully withdrawn money from player.");
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.FAILURE, "Must specify world");
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), worldName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        FileConfiguration data = VLOfflinePlayer.getOfflinePlayer(player).getDataConfig();
        if (data.contains("economy." + worldName)) {
            data.set("economy." + worldName, round(
                    data.getDouble("economy." + worldName) + amount));
        } else {
            data.set("economy." + worldName, round(+amount));
        }
        VLOfflinePlayer.getOfflinePlayer(player).saveData();
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Successfully disposed money to player.");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    @Deprecated
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    @Deprecated
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(-1, -1, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not implemented in this economy implementation.");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    @Deprecated
    public boolean createPlayerAccount(String playerName) {
        throw new UnsupportedOperationException("Bank is not implemented in this economy implementation.");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        throw new UnsupportedOperationException("Bank is not implemented in this economy implementation.");
    }

    @Override
    @Deprecated
    public boolean createPlayerAccount(String playerName, String worldName) {
        throw new UnsupportedOperationException("Bank is not implemented in this economy implementation.");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        throw new UnsupportedOperationException("Bank is not implemented in this economy implementation.");
    }
}
