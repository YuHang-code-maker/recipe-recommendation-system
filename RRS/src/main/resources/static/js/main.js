//TO DO
(()=>{
	'use strict';
	const ROOT_URL = 'http://localhost:8085/api/recipes';
	const findAll = ()=>{
		const token = localStorage.getItem('token');
		$.ajax({
			type:'GET',
			url: ROOT_URL,
			dataType: 'json',
			headers: {
			    Authorization: `Bearer ${token}`
			},
			success:renderList
		});
	};
	
	const renderList = (recipes)=>{
		$('#recipeList').empty();

		if(recipes.length === 0){
		     $('#recipeList').html(`
		          <div class="col-12 text-center mt-4">
		              <h5 class="text-muted">No recipes found</h5>
		           </div>
		      `);
		    return;
		    }
		recipes.forEach(recipe=>{
			const html=`
				<div class="col-md-6 col-lg-4 mb-4">
			          <div class="card recipe-card h-100 shadow-sm">
			                  <img src="/images/${recipe.image}" class="card-img-top recipe-img">
			                   <div class="card-body text-center">
			                        <h5 class="card-title">${recipe.title}</h5>
			                        <button class="btn btn-warning infoButton" data-id="${recipe.id}">More Info</button>
			                   </div>
			          </div>
			    </div>	
			`;
			$('#recipeList').append(html);
		});
	};
	
	const findById=(id)=>{
		$.ajax({
			type:'GET',
			url: `${ROOT_URL}/${id}`,
			dataType: 'json',
			success:showDetails
		});
	}
	
	const showDetails =(recipe)=>{
		$('#recipeModalLabel').text(`${recipe.title}`);
		$('#modalImage').attr('src',`/images/${recipe.image}`);
		$('#modalInstructions').text(recipe.instructions);
		$('#modalIngredients').empty();
		let html = ``;
		recipe.ingredients.forEach(ing=>{
		        html += `<li>${ing.name}</li>`;
		  });
		$('#modalIngredients').html(html);
		$('#recipeModal').modal('show');
	};
	
	const findByTitle = (title)=>{
		$.ajax({
			type:'GET',
			url: `${ROOT_URL}/search?title=${encodeURIComponent(title)}`,
			dataType: 'json',
			success: renderList
		});
	};
	
	const login = ()=>{
		$.ajax({
		        type: 'POST',
		        url: 'http://localhost:8085/auth/login',
		        contentType: 'application/json',
		        data: JSON.stringify({
		            username: $('#username').val(),
		            password: $('#password').val()
		        }),
		        success: function(response) {
		            localStorage.setItem('token', response.token);
		            window.location.href = 'index.html';
		        },
		        error: function() {
		            alert('Login failed');
		        }
		    });
	};
	
	const logout = ()=>{
		localStorage.removeItem('token');
		localStorage.removeItem('role');
		window.location.href = 'login.html';
	}
	
	$(()=>{
		$(document).on("click",".infoButton",function(){
		    const id = $(this).data("id");
			findById(id);
		});
		$('#searchBtn').on('click',()=>{
			const title = $('#searchInput').val().trim();
			if(!title){
			        alert("Title cannot be empty");
			        return;
			}
			findByTitle(title);
		});
		
		$('#showAllBtn').on('click',()=>{
			$('#searchInput').val('');
			findAll();
		});
		
		$('#loginBtn').on('click', function(e) {
		    e.preventDefault();
		    login();
		});
		
		$('#logoutBtn').on('click',function() {
		    logout();
		});
		findAll();
	})
	
	
})();