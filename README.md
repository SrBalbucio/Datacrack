# Datacrack - O database prático e rápido
O Datacrack é um database NoSQL criado usando Java e IPC TCP, a ideia dele é ser o mais simples possível de usar, portanto espere códigos fáceis partindo dele.

Datacrack is a NoSQL database created simply using Java and IPC TCP, an idea of it is to be as simple as possible to use, so expect easier from it.

AVISO: O Datacrack ainda se encontra em desenvolvimento, caso encontre bugs por favor me mande mensagem no Discord ou Email (suporte@balbucio.xyz)
<br>
WARNING: Datacrack is still under development, if you find bugs please message me on Discord or Email (suporte@balbucio.xyz)

## Tipos de dado:
No Datacrack os dados são divididos em conjuntos chamados de DataPack, abaixo você encontrará todos os tipos de DataPack:
### RootDataPack
Esse é o conjunto de dados principal, é nele que a teia de DataPacks começa.
### DataPack
Literalmente, um conjunto de dados, nele você pode criar outros DataPacks ou simplesmente setar Dados.
### TempDataPack
Esse tipo de DataPack é usado para salvar dados de forma temporária, já que em um restart do servidor os dados se perdem.
### UserDataPack
Esse tipo de DataPack é usado para salvar dados para um determinado usuário, atualmente na versão v1.2 há muitos problemas e conflitos de teoria que ainda não tive tempo de corrigir

## Criando um servidor
Para criar um server Datacrack basta executar a JAR, ou seja, abra o console e use o comando java -jar Datacrack.jar.
<br>
Feito isso, seu servidor Datacrack já estará em execução e bastará apenas você configurá-lo (não é necessário tutoriais para isso, todas as explicações estão no próprio arquivo).
<br>
<br>
Para **donos de hospedagem** que pretendem criar servidores datacrack em escala usando Pterodactyl e precisam de um egg, podem me chamar no Discord e ajudarei com isto.

# Usando o Datacrack Client
Vamos começar a codar e por o Datacrack em prática, você verá o quão simples é:

## Adicionando as dependências
Se você usa gradle:
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  dependencies {
	        implementation 'com.github.SrBalbucio:Datacrack:Tag'
	}
 ```
Se você use maven:
```maven
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	<dependency>
	    <groupId>com.github.SrBalbucio</groupId>
	    <artifactId>Datacrack</artifactId>
	    <version>v1.0</version>
	</dependency>
```

## Criando uma instância do Datacrack
Para você começar a usar o Datacrack, primeiro você deve criar um, como mostrado abaixo:
```java
//A única coisa que você deve ter é uma conta admin do servidor
Datacrack datacrack = new Datacrack(new User(<name>, <pass>));
datacrack.getManager();
```
Feito isso, precisamos adicionar um Socket (server) que o nosso Datacrack irá conectar: (lembrando que você pode adicionar mais do que um)
```java
//Adicione o IP e a porta do seu servidor
datacrack.getManager().addSocket(ip, port);
```
Perfeito, seu datacrack já está pronto para uso!

## Criando nosso primeiro RootDataPack
Com tudo configurado direitinho, podemos começar a criar e salvar dados usando o Datacrack:
```java
Manager manager = datacrack.getManager();
RootDataPack root = manager.getDefaultRootPack(); //retorna o RootDataPack padrão
RootDataPack abobrinha = manager.getRootPack("abobrinha");
abobrinha.setString("melancia", "melancia");
```

## Criando nosso primeiro DataPack
Com RootDataPack em mãos vamos criar um DataPack dentro dele:
```java
DataPack pack = abobrinha.createDataPack("sementes");
pack.setInt("chanceDeNascer", 60);
pack.getInt("chanceDeNascer");
pack.setInt("quantidade", 78);
```

## Criando nosso primeiro TempDataPack
Sem muitos segredos, vamos criar nosso primeiro dado temporário:
```java
TempDataPack temp = manager.createTempPack("arroz");
temp.setBoolean("queimado", true);
```
### Informações
Os dados assim que alterados são savos automaticamente, não há necessidade de códigos extras para isso.

Essa documentação ainda está incompleta, pretendo ir atualizando com tempo!

Discord: https://discord.gg/Y2V8EfrYA4
Email: suporte@balbucio.xyz
