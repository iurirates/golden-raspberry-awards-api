# Golden Raspberry Awards API

API RESTful para leitura da lista de indicados e vencedores da categoria **Pior Filme** do Golden Raspberry Awards.

Ao iniciar, a aplicação lê o arquivo `Movielist.csv`, carrega os dados em um banco **H2 em memória** e expõe endpoints REST — incluindo o cálculo dos produtores com **maior** e **menor** intervalo entre dois prêmios consecutivos.

## Tecnologias

- **Java 21** — usando *records*, *pattern matching* para `instanceof`, `String.formatted()` e **Virtual Threads** (JEP 444) habilitadas para o servidor web.
- **Spring Boot 3.5.14** (Spring Web, Spring Data JPA, Bean Validation).
- **Lombok** — reduz boilerplate em entidades, beans Spring (`@RequiredArgsConstructor`, `@Slf4j`) e em todos os records (`@With`).
- **MapStruct 1.6.3** — geração em tempo de compilação do mapper entidade ↔ DTO. `lombok-mapstruct-binding` garante a ordem correta dos annotation processors.
- **springdoc-openapi 2.8.14** (Swagger UI / OpenAPI 3).
- **H2 Database** embarcado em memória (nenhuma instalação externa necessária).
- **OWASP Dependency-Check** (plugin Gradle) — varredura de CVEs nas dependências.
- **Gradle** (build) e **JUnit 5 + MockMvc** (testes de integração).

## Pré-requisitos

- JDK 21 instalado e configurado (`java -version` deve apontar para a versão 21).
- Acesso à internet na primeira execução (para o Gradle baixar as dependências).

## Como rodar a aplicação

Na raiz do projeto:

```bash
# Linux / macOS
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

A aplicação sobe em `http://localhost:8080`.

> **Observação sobre o Gradle Wrapper:** os scripts `gradlew`/`gradlew.bat` e o
> `gradle-wrapper.properties` já estão incluídos. Caso o binário
> `gradle/wrapper/gradle-wrapper.jar` não esteja presente no seu ambiente,
> gere-o uma única vez com uma instalação local do Gradle:
>
> ```bash
> gradle wrapper --gradle-version 8.10.2
> ```
>
> Alternativamente, basta abrir o projeto em uma IDE (IntelliJ IDEA, Eclipse,
> VS Code), que o wrapper é resolvido automaticamente.

> **Lombok na IDE:** habilite o plugin do Lombok e o *Enable annotation
> processing* (IntelliJ: *Settings → Build → Compiler → Annotation Processors*).
> Sem isso, a IDE não enxerga getters/setters/loggers gerados.

## Como rodar os testes de integração

```bash
# Linux / macOS
./gradlew test

# Windows
gradlew.bat test
```

O relatório HTML é gerado em `build/reports/tests/test/index.html`.

Os testes de integração sobem o contexto completo do Spring (incluindo a carga
do CSV) e validam, via `MockMvc`, que os dados retornados estão de acordo com o
arquivo fornecido:

- `AwardIntervalIntegrationTest` — valida o endpoint contra o `Movielist.csv` fornecido.
- `MovieControllerIntegrationTest` — valida a carga do CSV (206 filmes) e o CRUD de filmes.
- `AwardIntervalScenarioIntegrationTest` — valida o algoritmo com um **conjunto de dados diferente**, cobrindo empates no menor e no maior intervalo, produtor com 3+ vitórias e registros que devem ser ignorados.
- `AwardIntervalEmptyScenarioIntegrationTest` — cenário-limite em que nenhum produtor venceu duas vezes (retorno `min`/`max` vazios).

### Precisão independente dos dados de entrada

O algoritmo é genérico: não há valores fixos no código de produção. Os testes
de cenário acima carregam CSVs alternativos (via property
`application.csv.movies-file`, com `spring.datasource.url` isolado por contexto)
para comprovar a corretude em situações que o arquivo original não exercita —
em especial **empates**, nos quais todos os produtores com o mesmo intervalo
extremo são retornados. O parser de produtores também trata a **vírgula de
Oxford** (`"A, B, and C"`), separação por vírgula e por "and", além de nomes
que contêm "and" (ex.: "Anderson").

## Varredura de vulnerabilidades

O plugin **OWASP Dependency-Check** está configurado no `build.gradle` para
falhar o build em qualquer CVE com CVSS ≥ 7.0:

```bash
./gradlew dependencyCheckAnalyze -PnvdApiKey=SEU_API_KEY
```

A API key da NVD (https://nvd.nist.gov/developers/request-an-api-key) é gratuita
e acelera bastante a primeira sincronização do banco de CVEs. O relatório fica
em `build/reports/dependency-check-report.html`.

## Endpoints

Nível 2 de maturidade de Richardson (recursos por URI + verbos e status HTTP).

### Intervalo de prêmios (requisito principal)

```
GET /api/producers/award-intervals
```

Resposta (dados reais do `Movielist.csv` fornecido):

```json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```

> Em caso de empate (mais de um produtor com o mesmo intervalo mínimo ou máximo),
> todos são retornados nos respectivos arrays.

### CRUD de filmes (endpoints auxiliares)

| Método | URI                | Descrição                  | Status de sucesso |
|--------|--------------------|----------------------------|-------------------|
| GET    | `/api/movies`      | Lista todos os filmes      | 200 OK            |
| GET    | `/api/movies/{id}` | Busca um filme por id      | 200 OK            |
| POST   | `/api/movies`      | Cria um filme              | 201 Created       |
| PUT    | `/api/movies/{id}` | Atualiza um filme          | 200 OK            |
| DELETE | `/api/movies/{id}` | Remove um filme            | 204 No Content    |

Exemplo de corpo para POST/PUT:

```json
{
  "year": 2024,
  "title": "Exemplo",
  "studios": ["Estúdio A", "Estúdio B"],
  "producers": ["Produtor 1", "Produtor 2"],
  "winner": false
}
```

## Documentação interativa (Swagger)

Com a aplicação no ar:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Console do banco H2

- `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:goldenraspberry`
- Usuário: `sa` — Senha: *(em branco)*

## Estrutura e decisões de arquitetura (SOLID)

```
com.outsera.goldenraspberry
├── config                         → Configuração do OpenAPI/Swagger
├── controller                     → Implementações REST
│   ├── api                        → Contratos REST (ProducerApi, MovieApi) com rotas + Swagger
│   └── domain
│       ├── request                → MovieRequest
│       └── response               → MovieResponse, AwardIntervalsResponse
├── domain                         → ProducerInterval (modelo do caso de uso)
├── entity                         → Entidades JPA (Movie, Producer)
├── exception                      → Exceções e tratamento global de erros
├── loader                         → Carga inicial dos dados (ApplicationRunner)
├── mapper                         → MovieMapper (MapStruct)
├── parser                         → Leitura/parse da fonte de dados (independente de formato)
│   ├── (interfaces)               → MovieParser, ProducerNameParser
│   ├── impl                       → CsvMovieParser (única ciente do CSV), DelimitedProducerNameParser
│   └── domain                     → ParsedMovie (modelo do filme já lido)
├── repository                     → Spring Data JPA
└── service                        → Casos de uso
    └── impl                       → AwardIntervalServiceImpl, MovieServiceImpl
```

Aplicação dos princípios SOLID:

- **S (Responsabilidade Única):** cada classe tem um papel claro — o parser do
  CSV (`CsvMovieParser`), a separação de nomes de produtores
  (`DelimitedProducerNameParser`), a persistência (`DefaultMovieImporter`), o
  cálculo de intervalos (`AwardIntervalServiceImpl`) e o mapeamento entidade↔DTO
  (`MovieMapper`, MapStruct) são responsabilidades isoladas.
- **O (Aberto/Fechado):** o contrato `MovieParser` é independente de formato;
  uma nova fonte (JSON, XML…) pode ser adicionada sem alterar quem consome.
- **L (Substituição de Liskov):** as implementações respeitam integralmente os
  contratos das interfaces.
- **I (Segregação de Interfaces):** interfaces pequenas e coesas — `MovieParser`,
  `ProducerNameParser`, `MovieImporter`, `AwardIntervalService`, `MovieService`,
  `MovieMapper`.
- **D (Inversão de Dependência):** as classes dependem de abstrações injetadas
  pelo Spring, nunca de implementações concretas.

Na camada web, o **contrato da API é separado da implementação**: as interfaces
`ProducerApi` e `MovieApi` concentram o mapeamento das rotas e a documentação
OpenAPI/Swagger, enquanto `ProducerController` e `MovieController` apenas as
implementam e delegam aos serviços.

### Lombok e MapStruct

- **Entidades JPA** (`Movie`, `Producer`) usam `@Getter`, `@Setter`,
  `@NoArgsConstructor(access = PROTECTED)` (exigido pelo JPA) e
  `@EqualsAndHashCode(of = "name")` em `Producer` para identidade por chave de
  negócio.
- **Beans Spring** (`MovieServiceImpl`, `AwardIntervalServiceImpl`,
  `CsvMovieParser`, `MovieDataLoader`, `DefaultMovieImporter`,
  `MovieController`, `ProducerController`) usam `@RequiredArgsConstructor`,
  eliminando o boilerplate de construtor com `final`.
- **`MovieDataLoader`** usa `@Slf4j` para o logger.
- **Records imutáveis** (`MovieRequest`, `MovieResponse`,
  `AwardIntervalsResponse`, `ParsedMovie`, `ProducerInterval`, `ApiError`)
  expõem `withX(...)` graças ao `@With`.
- **`MovieMapper`** é uma interface anotada com `@Mapper(componentModel = "spring")`:
  o MapStruct gera `MovieMapperImpl` em tempo de compilação e o Spring o
  injeta automaticamente. Sem reflexão em runtime, sem implementação manual.

### Estilo de código

O código de produção e de teste é mantido **sem comentários**: nomes
descritivos, classes pequenas e métodos curtos cumprem esse papel.

### Regra de negócio do cálculo de intervalos

Os produtores no CSV podem vir separados por vírgula **e/ou** pela palavra
"and" (ex.: `Bob Cavallo, Joe Ruffalo and Steve Fargnoli`). Cada nome é tratado
individualmente — incluindo a vírgula de Oxford. Para cada produtor com dois
ou mais prêmios, calcula-se o intervalo entre vitórias **consecutivas**; em
seguida, identificam-se os menores e maiores intervalos globais.
