package CryptoWallet;

public class Crypto {
    String name;
    double totalValue;
    double holding;
    double changePercentage;
    String symbol;

    static CryptoInfo[] cryptoInfo = {
        new CryptoInfo("Bitcoin", 17122.66, "BTC", -0.1),
        new CryptoInfo("Ethereum", 1271.12, "ETH", -0.3),
        new CryptoInfo("Dogecoin", 0.104016, "DOGE", 0.5),
        new CryptoInfo("Polkadot", 5.58, "DOT", -0.5),
        new CryptoInfo("Litecoin", 76.74, "LTC", 0.1),
    };

    public static void showCryptoInfo() {
        System.out.println("Market Rate");
        System.out.println("------");
        for(int i=0; i<cryptoInfo.length; i++) {
            System.out.println("Name: " + cryptoInfo[i].name);
            System.out.println("Symbol: " + cryptoInfo[i].symbol);
            System.out.println("Value: " + cryptoInfo[i].value);
            System.out.println("Change Percentage: " + cryptoInfo[i].changePercentage + "%");
            System.out.println("---");
        }
    }

    public Crypto(String symbol, double holding) throws InvalidCrypto {
        for(int i=0; i<cryptoInfo.length; i++) {
            if(cryptoInfo[i].symbol.equals(symbol)) {
                this.name = cryptoInfo[i].name;
                this.totalValue = cryptoInfo[i].value * holding;
                this.changePercentage = cryptoInfo[i].changePercentage;
                this.symbol = symbol;
                this.holding = holding;
                return;
            }
            if(i==cryptoInfo.length-1 && cryptoInfo[i].symbol != symbol) {
                throw new InvalidCrypto(symbol + " Crypto is not available in our system.");
            }
        }
    }
}

class CryptoInfo {
    String name;
    double value;
    String symbol;
    double changePercentage;

    public CryptoInfo(String name, double value, String symbol, double changePercentage) {
        this.name = name;
        this.value = value;
        this.symbol = symbol;
        this.changePercentage = changePercentage;
    }
}
