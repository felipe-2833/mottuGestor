# Mottu Gestor

O Mottu Gestor é uma solução inteligente de mapeamento e gestão de pátios para frotas de motos. Utilizando RFID, nosso sistema oferece rastreamento em tempo real, otimização operacional e segurança automatizada, reduzindo custos e aumentando a eficiência da Mottu.

## Destaques da solução
- ✅ Localização precisa de motos em pátios, eliminando buscas manuais e otimizando tempo.
- ✅ Relatórios de ocupação e histórico de movimentações.
- ✅ Tecnologia RFID de baixo custo.
- ✅ Escalável para múltiplas filiais e adaptável a outros tipos de veículos.

## Funcionalidades

- *Cadastro de Motos, Movimentações, Pátios, Leitores e Usuários:* Permite o registro no sistema de informações.

- *Atualização de Informações:* Possibilidade de editar ou excluir informações.

- *Visualização de Informações:* Exibe os detalhes da categoria desejada, garantindo transparência e facilidade no gerenciamento de dados.

- *API REST em Java*

- *Banco de Dados H2*

## Requisitos

- Java JDK 17+
- Maven 3.8+
- Docker (opcional para containerização)

## Execução do projeto (Visual Studio Code)

1. Clone o repositório da api:

```bash
git clone https://github.com/felipe-2833/mottuGestor.git
```

2. Entre na pasta do projeto

```bash
cd mottuGestor
```

3. Dê run no projeto (Run Java)

#

#### Não esqueça de conferir os caminhos para rodar os comandos e inicializar a aplicação da forma correta!

#

## Instruções para envio de requisições

Acesse o Swagger: http://localhost:8080/swagger-ui/index.html

### 1. Criar moto (POST)
**Endpoint**: `POST /motos`

Passos:

1. Clique em POST /motos

2. Clique em "Try it out"

3. Substitua o exemplo pelo JSON abaixo:
```json
{
  "placa": "ABC1D23",
  "modelo": "Honda CG 160",
  "rfid_tag": "TAG123456",
  "data_cadastro": "2023-05-15",
  "servico": "Entregas Rápidas"
}
```
4. Clique em "Execute"

### 2. Listar todas as motos (GET)
**Endpoint**: `GET /motos`

Parâmetros de filtro de pesquisa:

| Parâmetro   | Exemplo    | Descrição                |
| ----------- | ---------- | ------------------------ |
| `placa`     | ABC1D23    | Filtra por placa         |
| `modelo`    | Honda      | Filtra por modelo        |
| `servico`   | Entregas   | Filtra por serviço       |
| `startDate` | 2023-01-01 | Data inicial de cadastro |
| `endDate`   | 2023-12-31 | Data final de cadastro   |

Parâmetros de paginação:

| Parâmetro | Tipo    | Padrão     | Exemplo    | Descrição                               |
| --------- | ------- | ---------- | ---------- | --------------------------------------- |
| `page`    | Integer | 0          | 1          | Número da página (começa em 0)          |
| `size`    | Integer | 5          | 10         | Quantidade de itens por página          |
| `sort`    | String  | placa,desc | modelo,asc | Campo(s) para ordenação (campo,direção) |


### 3. Buscar moto por ID (GET)
**Endpoint**: `GET /motos/{id_moto}`

Passos:

1. Clique em GET /motos/{id_moto}

2. Clique em "Try it out"

3. Preencha o id_moto (ex: 1)

4. Clique em "Execute"

### 4. Atualizar moto (PUT)
**Endpoint**: `PUT /motos/{id_moto}`

1. Clique em PUT /motos/{id_moto}

2. Clique em "Try it out"

3. Preencha o id_moto (deve bater com o ID no JSON, ex: 1)

4. Substitua o corpo com:

```json
{
  "id_moto": 1,
  "placa": "XYZ9A87",
  "modelo": "Yamaha Factor 150",
  "rfid_tag": "TAG654321",
  "data_cadastro": "2023-06-20",
  "servico": "Entregas Expressas"
}
```

5. Clique em "Execute"

### 5. Apagar moto (DELETE)
**Endpoint**: `DELETE /motos/{id_moto}`

1. Clique em DELETE /motos/{id_moto}

2. Clique em "Try it out"

3. Preencha o id_moto (ex: 1)

4. Clique em "Execute"

## Execução do projeto (com Docker)

No Azure CLI, digite: 

```json
vi deploy_script.sh
```


Digite "i" para inserir o seguinte script:
```json
RESOURCE_GROUP="meuGrupoDeRecursos"
LOCATION="brazilsouth"  # ou sua região preferida
VM_NAME="minhaVM"
ADMIN_USERNAME="azureuser"
IMAGE="almalinux:almalinux-x86_64:9-gen2:9.5.202411260"
PORT=8080

az group create --name $RESOURCE_GROUP --location $LOCATION

az vm create \
  --resource-group $RESOURCE_GROUP \
  --name $VM_NAME \
  --image $IMAGE \
  --admin-username $ADMIN_USERNAME \
  --generate-ssh-keys \
  --public-ip-sku Standard

az vm open-port --resource-group $RESOURCE_GROUP --name $VM_NAME --port $PORT

echo "Esperando provisionamento..."
sleep 10

PUBLIC_IP=$(az vm list -d -o tsv --query [0].publicIps)
echo "IP da VM: $PUBLIC_IP"

echo "Conecte na VM via ssh e execute os comandos de instalação do Docker"

echo "ssh $ADMIN_USERNAME@$PUBLIC_IP"

cat << EOF
# Após conectar na VM com:
ssh $ADMIN_USERNAME@$PUBLIC_IP

# Execute os seguintes comandos na VM:

sudo dnf remove -y podman-docker
sudo dnf install -y docker-ce docker-ce-cli containerd.io
sudo systemctl start docker
sudo systemctl enable docker
sudo yum install -y git
sudo dnf install -y maven
git clone https://github.com/felipe-2833/mottuGestor.git
cd mottuGestor
mvn clean package
docker build -t myapp-image .
docker run -d -p 8080:8080 myapp-image
curl http://{IP_DA_VM}/swagger-ui/index.html

EOF
```

```json
sudo dnf remove -y podman-docker
sudo dnf install -y docker-ce docker-ce-cli containerd.io
sudo systemctl start docker
sudo systemctl enable docker
sudo yum install -y git
sudo dnf install -y maven
git clone https://github.com/felipe-2833/mottuGestor.git
cd mottuGestor
mvn clean package
docker build -t myapp-image .
docker run -d -p 8080:8080 myapp-image
curl http://{IP_DA_VM}/swagger-ui/index.html
```

Depois de colar, pressione Esc, digite: ":wq"

```json
chmod +x deploy_script.sh
```

```json
./deploy_script.sh
```

Depois, siga as instruções listadas!

## Equipe

- Felipe Levy Stephens Fidelix - *RM: 556426*
- Jennifer Kaori Suzuki - *RM: 554661*
- Samir Hage Neto - *RM: 557260*