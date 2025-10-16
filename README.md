# Projeto - Labirinto de Creta 

Trabalho prático da disciplina de **Algoritmos em Grafos**  
Desenvolvido em **Java**.

---

## Descrição do Projeto

O objetivo deste projeto é **simular a travessia de um labirinto** por um personagem (Herói), enquanto é perseguido por um **Minotauro**.  
A simulação utiliza conceitos de **grafos** e **algoritmos clássicos** de busca e caminhos mínimos (como **DFS** e **Dijkstra**).

Durante a execução:
- O **Entrante** explora o labirinto utilizando uma estratégia de **busca com retrocesso (backtracking)**, armazenando o caminho percorrido com um "novelo de lã".
- O **Minotauro** patrulha o labirinto utilizando **DFS** e, ao detectar o herói dentro de um determinado raio, passa a persegui-lo utilizando **Dijkstra** (menor caminho).
- A simulação considera também **tempo máximo de sobrevivência** (representando a comida do Entrante) e **combate direto** caso ambos se encontrem, com 1% de chance de vitória do herói.

O resultado da simulação é registrado em um **relatório de texto (`relatorio_simulacao.txt`)**, contendo os eventos relevantes, rodadas e resultado final.
Cada rodada é apresentada devidamente no terminal.

---

## Instruções de Execução

### ✅ Pré-requisitos
- **Java JDK 17** (ou superior)
- **IDE** como IntelliJ IDEA, Eclipse, VS Code com extensão Java, ou execução via terminal

---

### ▶ Executando o Projeto


1. **Verifique a estrutura da pasta _resources/_:**:
- O arquivo labirinto.txt deve estar dentro da pasta resources,
  e a pasta deve estar marcada corretamente como "Resources Root" no projeto da IDE.
- Caso contrário, o programa exibirá:

>FileNotFoundException: labirinto.txt não encontrado no classpath

- Solução:
No IntelliJ IDEA, clique com o botão direito na pasta resources/ →
"Mark Directory as" → "Resources Root".

2. **Execute a classe principal**:
- Local: simulacao.Simulacao

- As mensagens do andamento da simulação aparecem no console.

- O resultado completo é salvo no arquivo relatorio_simulacao.txt.

---

## Vídeos 



| Integrante      | Vídeo | Parte explicada  |
|-----------------|-------|------------------|
| Marina de Sousa | [🔗 Vídeo 1](https://youtu.be/InsDH73XESc?si=wMsJf0hBKIyzc3p9) | Grafo e Entrante |
| Geovanna Cabral | [🔗 Vídeo 2](https://youtu.be/) | Minotauro        |
| Paula Hânnia    | [🔗 Vídeo 3](https://youtu.be/Efog-Hg5as0?si=pJ8xORfAhbCE6rsB) | Simulação        |
