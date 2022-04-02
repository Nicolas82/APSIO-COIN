<h1 align="center">APSIO Node</h1>

<p align="center">
  <a href="https://github.com/wavesplatform/Waves/actions" target="_blank">
    <img alt="stars" src="https://badgen.net/github/stars/Nicolas82/APSIO-COIN"  />
  </a>
  <a href="https://github.com/wavesplatform/Waves/releases" target="_blank">
    <img alt="release" src="https://badgen.net/github/release/Nicolas82/APSIO-COIN" />
  </a>
</p>

> APSIO is an open source blockchain protocol based on [Waves protocol](https://waves.tech/waves-protocol). <br/> 
> This is a blockchain protocol built for the domain of education. You can use it to build your own decentralized applications. APSIO provides full blockchain ecosystem including smart contracts language  based on RIDE (Waves smart contracts language).


## ✨ Functionalities

APSIO node is a host connected to the blockchain network with the following functions:

- Processing and validation of transactions
- Generation and storage of blocks
- Network communication with other nodes
- REST API
- Extensions management

## 🚀️ Getting started

A quick introduction of the minimal setup you need to get a running node. 

*Prerequisites:*
- configuration file for a needed network from [here](https://github.com/wavesplatform/Waves/tree/HEAD/node)
- `waves-all*.jar` file from [releases](https://github.com/wavesplatform/Waves/releases) 

Linux systems:
```bash
sudo apt-get update
sudo apt-get install openjdk-8-jre
java -jar node/target/waves-all*.jar path/to/config/waves-{network}.conf
```

Mac systems (assuming already installed homebrew):
```bash
brew cask install adoptopenjdk/openjdk/adoptopenjdk8
java -jar node/target/waves-all*.jar path/to/config/waves-{network}.conf
```

Windows systems (assuming already installed OpenJDK 8):
```bash
java -jar node/target/waves-all*.jar path/to/config/waves-{network}.conf
```

Using docker, follow the [official image documentation](https://hub.docker.com/r/wavesplatform/wavesnode).

> More details on how to install a node for different platforms you can [find in the documentation](https://docs.waves.tech/en/waves-node/how-to-install-a-node/how-to-install-a-node). 

## 🔧 Configuration

The best starting point to understand available configuration parameters is [this article](https://docs.waves.tech/en/waves-node/node-configuration).

The easiest way to start playing around with configurations is to use default configuration files for different networks; they're available in [network-defaults.conf](./node/src/main/resources/network-defaults.conf).

Logging configuration with all available levels and parameters is described [here](https://docs.waves.tech/en/waves-node/logging-configuration).

## 👨‍💻 Development

The node can be built and installed wherever Java can run. 
To build and test this project, you will have to follow these steps:

<details><summary><b>Show instructions</b></summary>

*1. Setup the environment.*
- Install Java for your platform:

```bash
sudo apt-get update
sudo apt-get install openjdk-8-jre                     # Ubuntu
# or
# brew cask install adoptopenjdk/openjdk/adoptopenjdk8 # Mac
```

- Install SBT (Scala Build Tool)

Please follow the SBT installation instructions depending on your platform ([Linux](https://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Linux.html), [Mac](https://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Mac.html), [Windows](https://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Windows.html))

*2. Clone this repo*

```bash
git clone https://github.com/wavesplatform/Waves.git
cd Waves
```

*3. Compile and run tests*

```bash
sbt checkPR
```

*4. Run integration tests (optional)*

Create a Docker image before you run any test: 
```bash
sbt node-it/docker
```

- Run all tests. You can increase or decrease number of parallel running tests by changing `waves.it.max-parallel-suites`
system property:
```bash
sbt -Dwaves.it.max-parallel-suites=1 node-it/test
```

- Run one test:
```bash
sbt node-it/testOnly *.TestClassName
# or 
# bash node-it/testOnly full.package.TestClassName
```

*5. Build packages* 

```bash
sbt packageAll                   # Mainnet
sbt -Dnetwork=testnet packageAll # Testnet
```

`sbt packageAll` ‌produces only `deb` package along with a fat `jar`. 

*6. Install DEB package*

`deb` package is located in target folder. You can replace '*' with actual package name:

```bash
sudo dpkg -i node/target/*.deb
```


*7. Run an extension project locally during development (optional)*

```bash
sbt "extension-module/run /path/to/configuration"
```

*8. Configure IntelliJ IDEA (optional)*

The majority of contributors to this project use IntelliJ IDEA for development, if you want to use it as well please follow these steps:

1. Click `Add configuration` (or `Edit configurations...`).
2. Click `+` to add a new configuration, choose `Application`.
3. Specify:
   - Main class: `com.wavesplatform.Application`
   - Program arguments: `/path/to/configuration`
   - Use classpath of module: `extension-module`
4. Click `OK`.
5. Run this configuration.

</details>

## 🤝 Contributing

If you'd like to contribute, please fork the repository and use a feature branch. Pull requests are warmly welcome.

For major changes, please open an issue first to discuss what you would like to change. Please make sure to update tests as appropriate.

Please follow the [code of conduct](./CODE_OF_CONDUCT.md) during communication with the each other. 

## ⛓ Links

- [Documentation](https://docs.waves.tech/)
- Blockchain Explorer: [Mainnet](https://apsio02.iut-blagnac.fr/explorer)
- [Ride Online IDE](https://waves-ide.com/)

## 📝 Licence

The code in this project is licensed under [MIT license](./LICENSE)

## 👏 Acknowledgements

### Waves

As the project is forked from [Waves protocol](https://github.com/wavesplatform/Waves) we say a big thank you for building a such incredible blockchain.
<br>
We hope that Waves will become one of the largest blockchain of the world, with a living ecosystem.
