function verify(form){
    let utilizador = document.getElementById("utilizador").value;
    let password = document.getElementById("password").value;
    if ( form.utilizador.value == "adm" && form.password.value == "adm"){
      alert ("Login com sucesso");
      window.location.href = "Bem_Vindo.html";
    }
    else{
      alert("Nome de utilizador ou Password errados");
      }
    }
  
    function register(form){
      let utilizador = document.getElementById("utilizador").value;
      let password = document.getElementById("password").value;
      if(form.utilizador.value && form.password.value){
        alert("Registado com sucesso");
        window.location = "Login.html";
      }
      else{
        alert("Preencha os dados corretamente");
      }
    }