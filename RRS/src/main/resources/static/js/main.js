(()=>{
	'use strict';
	const ROOT_URL = 'http://localhost:8085/api/recipes';
	
	let currentRecipeId = null;
	let currentRecipe = null;
	let recipeChart = null;
	
	const showLoginView = () => {
		$('#loginView').removeClass('d-none');
		$('#appView').addClass('d-none');
	};

	const showAppView = () => {
		$('#loginView').addClass('d-none');
		$('#appView').removeClass('d-none');
	};
	
	const initApp = () => {
	    const token = localStorage.getItem('token');

		if (token) {
		     findAll();
		} else {
		     showLoginView();
		}
	};
	
	const findAll = ()=>{
		const token = localStorage.getItem('token');
		$.ajax({
			type:'GET',
			url: ROOT_URL,
			dataType: 'json',
			headers: {
			    Authorization: `Bearer ${token}`
			},
			success: function(recipes) {
				const currentToken = localStorage.getItem('token');
				if (!currentToken) return;
			            showAppView();
			            applyRolePermissions();
			            renderList(recipes);
			 },
			 error: function(xhr) {
			       if (xhr.status === 401) {
			                alert('Please log in again.');
			                logout();
			        } else {
			                alert('Failed to load recipes.');
			        }
			 }
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
		const token = localStorage.getItem('token');
		$.ajax({
			type:'GET',
			url: `${ROOT_URL}/${id}`,
			dataType: 'json',
			headers: {
				Authorization: `Bearer ${token}`
			},
			success:showDetails
		});
	}
	
	const showDetails =(recipe)=>{
		currentRecipe = recipe;
		currentRecipeId = recipe.id;
		$('#recipeModalLabel').text(`${recipe.title}`);
		$('#modalImage').attr('src',`/images/${recipe.image}`);
		$('#modalInstructions').text(recipe.instructions);
		$('#modalIngredients').empty();
		if (recipe.externalLink) {
		    $('#modalExternalLink')
		        .attr('href', recipe.externalLink)
		        .show();
		} else {
		    $('#modalExternalLink')
		        .hide()
		        .attr('href', '#');
		}
		let html = ``;
		recipe.ingredients.forEach(ing=>{
		        html += `<li>${ing.name}</li>`;
		  });
		$('#modalIngredients').html(html);
		$('#recipeModal').modal('show');
	};
	
	const findByTitle = (title)=>{
		const token = localStorage.getItem('token');
		$.ajax({
			type:'GET',
			url: `${ROOT_URL}/search?title=${encodeURIComponent(title)}`,
			dataType: 'json',
			headers: {
				Authorization: `Bearer ${token}`
			},
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
					console.log('login response:', response);
					localStorage.setItem('token', response.token);

					if (response.role) {
					    localStorage.setItem('role', response.role);
					}
					showAppView();
					applyRolePermissions();
					findAll();
		        },
		        error: function() {
		            alert('Login failed');
		        }
		    });
	};
	
	const applyRolePermissions = () => {
	    const role = localStorage.getItem('role');

	    if (role === 'ROLE_admin') {
	        $('.admin-only').show();
	    } else {
	        $('.admin-only').hide();
	    }
	};
	
	const logout = ()=>{
		localStorage.removeItem('token');
		localStorage.removeItem('role');
		
		currentRecipeId = null;
		currentRecipe = null;

		$('#recipeList').empty();
		$('#searchInput').val('');
		$('#searchInputIngredient').val('');
		document.getElementById('loginForm').reset();

		$('#recipeModal').modal('hide');
		$('#addRecipeModal').modal('hide');

		showLoginView();
	};
	
	const addRecipe = () => {
	    const token = localStorage.getItem('token');

	    const title = $('#recipeTitle').val().trim();
	    const image = $('#recipeImage').val().trim();
	    const instructions = $('#recipeInstructions').val().trim();
	    const ingredientsInput = $('#recipeIngredients').val().trim();
		let externalLink = $('#recipeExternalLink').val().trim();

	    if (!title || !image || !instructions || !ingredientsInput) {
	        $('#addRecipeError').text('All fields are required.').show();
	        return;
	    }
		
		if (externalLink && !externalLink.startsWith('http://') && !externalLink.startsWith('https://')) {
		    externalLink = 'https://' + externalLink;
		}

	    const ingredients = ingredientsInput
	        .split(',')
	        .map(name => name.trim())
	        .filter(name => name !== '')
	        .map(name => ({ name: name }));

	    const recipeData = {
	        title: title,
	        image: image,
	        instructions: instructions,
			externalLink: externalLink,
	        ingredients: ingredients
	    };

	    $.ajax({
	        type: 'POST',
	        url: ROOT_URL,
	        contentType: 'application/json',
	        headers: {
	            Authorization: `Bearer ${token}`
	        },
	        data: JSON.stringify(recipeData),
	        success: () =>{
				$('#addRecipeModal').modal('hide');
				document.getElementById('addRecipeForm').reset();
				

				currentRecipeId = null;
				currentRecipe = null;
				findAll();
	        },
	        error: function(xhr) {
	            console.log('addRecipe failed:', xhr.status, xhr.responseText);
				let message = "Failed to add recipe.";

				if (xhr.responseJSON && xhr.responseJSON.errorMessage) {
				      message = xhr.responseJSON.errorMessage;
				}

				$('#addRecipeError').text(message).show();
	        }
	    });
	};
	
	const deleteRecipe = (id) => {
	    const token = localStorage.getItem('token');

	    $.ajax({
	        type: 'DELETE',
	        url: `${ROOT_URL}/${id}`,
	        headers: {
	            Authorization: `Bearer ${token}`
	        },
	        success: function() {
	            $('#recipeModal').modal('hide');
	            currentRecipeId = null;
	            findAll();
	        },
	        error: function(xhr) {
	            console.log('deleteRecipe failed:', xhr.status, xhr.responseText);

	            let message = 'Failed to delete recipe.';

	            if (xhr.responseJSON && xhr.responseJSON.errorMessage) {
	                message = xhr.responseJSON.errorMessage;
	            }

	            alert(message);
	        }
	    });
	};
	
	const openEditModal = (recipe) => {
		currentRecipeId = recipe.id;

		$('#recipeTitle').val(recipe.title);
		$('#recipeImage').val(recipe.image);
		$('#recipeInstructions').val(recipe.instructions);
		$('#recipeExternalLink').val(recipe.externalLink || '');

		const ingredients = recipe.ingredients.map(i => i.name).join(', ');
		$('#recipeIngredients').val(ingredients);
		$('#addRecipeModalLabel').text("Edit Recipe");
		$('#addRecipeModal').modal('show');
	};
			
	const updateRecipe = () => {
		const token = localStorage.getItem('token');
		
		const title = $('#recipeTitle').val().trim();
		const image = $('#recipeImage').val().trim();
		const instructions = $('#recipeInstructions').val().trim();
		const ingredientsInput = $('#recipeIngredients').val().trim();
		const externalLink = $('#recipeExternalLink').val().trim();
		const ingredients = ingredientsInput
			        .split(',')
			        .map(name => name.trim())
			        .filter(name => name !== '')
			        .map(name => ({ name }));
					
	 	const recipeData = {
			        id: currentRecipeId,
			        title,
			        image,
			        instructions,
					externalLink,
			        ingredients
		};

		$.ajax({
			 		type: 'PUT',
			        url: `${ROOT_URL}/${currentRecipeId}`,
			        contentType: 'application/json',
			        headers: {
			            Authorization: `Bearer ${token}`
			    },
			        data: JSON.stringify(recipeData),

			        success: function () {

			            $('#addRecipeModal').modal('hide');

			            currentRecipeId = null;
			            currentRecipe = null;

			            findAll();
			        },

			        error: function (xhr) {

			            let message = "Failed to update recipe";

			            if (xhr.responseJSON && xhr.responseJSON.errorMessage) {
			                message = xhr.responseJSON.errorMessage;
			            }

			            $('#addRecipeError').text(message).show();
			        }
			    });
			};
			
		const findByIngredients = (ingredientsInput) => {
			    const token = localStorage.getItem('token');

			    $.ajax({
			        type: 'GET',
			        url: `${ROOT_URL}/searchByIngredients?ingredients=${encodeURIComponent(ingredientsInput)}`,
			        dataType: 'json',
			        headers: {
			            Authorization: `Bearer ${token}`
			        },
			        success: renderList,
			        error: function(xhr) {
			            console.log('findByIngredients failed:', xhr.status, xhr.responseText);
			            let message = 'Failed to search recipes by ingredients.';
			            if (xhr.responseJSON && xhr.responseJSON.errorMessage) {
			                message = xhr.responseJSON.errorMessage;
			            }
			            alert(message);
			        }
			    });
			};
			
			const renderPopularityChart = () => {
			    const ctx = document.getElementById('recipeChart');

			    if (!ctx) {
			        return;
			    }

			    if (recipeChart) {
			        recipeChart.destroy();
			    }

			    recipeChart = new Chart(ctx, {
			        type: 'bar',
			        data: {
			            labels: ['Tomato Pasta', 'Fried Egg', 'Chicken Salad', 'Garlic Bread', 'Cheese Omelette'],
			            datasets: [{
			                label: 'Popularity Score',
			                data: [85, 92, 70, 65, 88],
			                borderWidth: 1
			            }]
			        },
			        options: {
			            responsive: true,
			            plugins: {
			                legend: {
			                    display: true
			                }
			            },
			            scales: {
			                y: {
			                    beginAtZero: true,
			                    max: 100
			                }
			            }
			        }
			    });
			};
	
	$(()=>{
		$(document).on("click",".infoButton",function(){
		    const id = $(this).data("id");
			findById(id);
		});
		if ($('#searchBtn').length) {
		        $('#searchBtn').on('click', () => {
		            const title = $('#searchInput').val().trim();
		            if (!title) {
		                alert("Title cannot be empty");
		                return;
		            }
		            findByTitle(title);
		     });
		 }
		
		 if ($('#showAllBtn').length) {
		         $('#showAllBtn').on('click', () => {
		             $('#searchInput').val('');
					 $('#searchInputIngredient').val('');
		             findAll();
		     });
		 }
		
		 if ($('#loginForm').length) {
		         $('#loginForm').on('submit', function(e) {
		             e.preventDefault();
		             console.log('form submitted');
		             login();
		         });
		     }
		
		if ($('#logoutBtn').length) {
			         $('#logoutBtn').on('click', function() {
			             logout();
			});
		}
		
		if ($('#addBtn').length) {
		    $('#addBtn').on('click', function() {
				currentRecipeId = null;
				currentRecipe = null;

				document.getElementById('addRecipeForm').reset();
				$('#addRecipeError').hide().text('');
				$('#addRecipeModalLabel').text("Add New Recipe");
				$('#addRecipeModal').modal('show');
		    });
		}
		
		if ($('#saveRecipeBtn').length) {
		    $('#saveRecipeBtn').on('click', function() {
				if (currentRecipeId) {
				     updateRecipe();
				} else {
				     addRecipe();
				}
		    });
		}
		
		if ($('#deleteBtn').length) {
		    $('#deleteBtn').on('click', function() {
		        if (!currentRecipeId) {
		            alert('No recipe selected.');
		            return;
		        }

		        const confirmed = confirm('Are you sure you want to delete this recipe?');

		        if (confirmed) {
		            deleteRecipe(currentRecipeId);
		        }
		    });
		}
		
		if ($('#editBtn').length) {
		    $('#editBtn').on('click', function () {

		        if (!currentRecipe) {
		            alert("No recipe selected");
		            return;
		        }

		        $('#recipeModal').modal('hide');

		        openEditModal(currentRecipe);
		    });
		}
		
		if ($('#searchBtnIngredient').length) {
		    $('#searchBtnIngredient').on('click', () => {
		        const ingredientsInput = $('#searchInputIngredient').val().trim();

		        if (!ingredientsInput) {
		            alert('Please enter at least one ingredient.');
		            return;
		        }

		        findByIngredients(ingredientsInput);
		    });
		}
		renderPopularityChart();
		initApp();
	})
	
	
})();