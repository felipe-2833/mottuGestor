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

echo "Conecte na VM via ssh e execute os comandos de instalação do Docker"

echo "ssh $ADMIN_USERNAME@$PUBLIC_IP"

cat << EOF
# Após conectar na VM com:
ssh $ADMIN_USERNAME@$PUBLIC_IP

# Execute os seguintes comandos na VM:

sudo dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
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
curl http://{$PUBLIC_IP}:8080/swagger-ui/index.html

EOF