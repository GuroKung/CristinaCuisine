angular.module('cristinaApp', ['ngStorage'])
  .service('ingredients', function ($localStorage) {
    var self = this
    self.ingredientList = []

    self.add = function (selected) {
      self.ingredientList.push({ name: selected })
      $localStorage.ingredientList = self.ingredientList
      console.log('added')
    }

    self.clear = function () {
      $localStorage.ingredientList = []
    }

    self.get = function () {
      return $localStorage.ingredientList
    }

    self.toString = function () {
      var str = ''
      for (var i = 0 ; i < self.ingredientList.length ; i++) {
        if (i === self.ingredientList.length - 1) {
          str += self.ingredientList[i].name
          break
        }
        str += self.ingredientList[i].name + ','
      }
      return str
    }
  })

  .service('recipeList', function () {
    var self = this
    self.recipeList = [{
      name: 'BoiledEgg',
      calories: 75,
      ingredients: [
        {
          name: 'Egg'
        }
      ],
      img: 'http://static.comicvine.com/uploads/original/11/118136/2482863-hard_boiled_egg.jpg'
    }, {
      name: 'SomTumThai',
      calories: 55,
      ingredients: [
        {
          name: 'Papaya'
        },
        {
          name: 'Tomato'
        },
        {
          name: 'Lime'
        },
        {
          name: 'Cowpea'
        },
        {
          name: 'Peanut'
        }
      ],
      img: 'http://urban-realtor.com/wp-content/uploads/2015/07/%E0%B8%AA%E0%B9%89%E0%B8%A1%E0%B8%95%E0%B8%B3%E0%B9%84%E0%B8%97%E0%B8%A2.jpg'
    }, {
      name: 'StirFriedPork',
      calories: 245,
      ingredients: [
        {
          name: 'Pork'
        },
        {
          name: 'Garlic'
        }
      ],
      img: 'http://www.cookhacker.com/wp-content/uploads/2010/04/Pork-Stir-Fry-SM.jpg'
    }]

    self.getRecipe = function () {
      return self.recipeList
    }
  })

  .controller('IngredientController', function (ingredients) {
    var self = this
    self.selected = 'Egg'
    ingredients.clear()
    self.ingredientList = ingredients.get()
    self.submitIngredient = ingredients.toString()

    self.addtoList = function (selected) {
      ingredients.add(selected)
      self.ingredientList = ingredients.get()
      self.submitIngredient = ingredients.toString()
    }
  })

  .controller('ResultController', function (recipeList, ingredients) {
    var self = this
    self.calPerMeal = 575.2
    self.ingredientList = ingredients.get()
    self.recipeList = recipeList.getRecipe()
    self.recommend = []

    console.log('Ingredient list: ')
    console.log(self.ingredientList)

    var isStirFriedPork = 0
    var isSomTumThai = 0
    for (var i = 0; i < self.ingredientList.length; i++) {
      if (self.ingredientList[i].name === 'Egg' && self.recipeList[0].calories <= self.calPerMeal) {
        self.recommend.push(self.recipeList[0])
      }
      else if (self.ingredientList[i].name === 'Pork' || self.ingredientList[i].name === 'Garlic') {
        isStirFriedPork++
      }
      else if (self.ingredientList[i].name === 'Papaya' || self.ingredientList[i].name === 'Tomato' || self.ingredientList[i].name === 'Lime' ||
        self.ingredientList[i].name === 'Cowpea' || self.ingredientList[i].name === 'Peanut') {
        isSomTumThai++
      }

      if (isStirFriedPork === 2 && self.recipeList[2].calories <= self.calPerMeal) {
        self.recommend.push(self.recipeList[2])
      }
      else if (isSomTumThai === 5 && self.recipeList[1].calories <= self.calPerMeal) {
        self.recommend.push(self.recipeList[1])
      }
    }
    console.log(self.recommend)
  })
