
packer {
  required_plugins {
    googlecompute = {
      version = "~> 1.0"
      source  = "github.com/hashicorp/googlecompute"
    }
  }
}

variable "source_file" {
  type    = string
  default = ""
}

variable "project_id" {
  description = "The GCP project ID"
  default     = "cloud-dev-415102" # Replace "your_project_id_here" with your actual project ID
}

variable "zone" {
  description = "The GCP zone"
  default     = "us-west1-b" # Replace "your_region_here" with your desired GCP region
}

# Define the builder for GCP
source "googlecompute" "test_image" {
  project_id   = var.project_id
  zone         = var.zone
  source_image = "centos-stream-8-v20240110"
  ssh_username = "root-test"
}


build {
  name = "test-packer"
  sources = [
    "source.googlecompute.test_image"
  ]

  provisioner "file" {
    source      = "${var.source_file}"
    destination = "/tmp/application-0.0.1-SNAPSHOT.jar"
  }

  provisioner "file" {
    source      = "systemd.service"
    destination = "/tmp/systemd.service"
  }

  provisioner "file" {
    source      = "opsconfig.yaml"
    destination = "/tmp/opsconfig.yaml"
  }


  provisioner "shell" {
    inline = [
      "sudo yum install unzip -y",
      "sudo cp /tmp/systemd.service /etc/systemd/system/systemd.service",
      "sudo yum install java-17-openjdk -y",
      "sudo groupadd csye6225",
      "sudo useradd -r  -m -g csye6225 -s /usr/sbin/nologin csye6225",
      "sudo mkdir -p /opt/your-app",
      "sudo cp /tmp/application-0.0.1-SNAPSHOT.jar /opt/your-app",
      "sudo chown -R csye6225:csye6225 /opt/your-app",
      "curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh",
      "sudo bash add-google-cloud-ops-agent-repo.sh --also-install",
      "sudo cp /tmp/opsconfig.yaml /etc/google-cloud-ops-agent/config.yaml",
      "sudo systemctl daemon-reload",
      "sudo systemctl enable systemd",
      "sudo systemctl restart google-cloud-ops-agent"
    ]
  }
}
