# -*- coding: utf-8 -*-

# names = ['anne', 'beth', 'george', 'damon']
# ages = [12, 45, 32, 102]
#
# for i in range(len(names)):
#     print names[i], 'is', ages[i], 'years old'
#
# for name, age in zip(names, ages):
#     print name, 'is', age, 'years old'

# index = 0
# for string in strings:
#     if 'xxx' in string:
#         string[index] = '[null]'
#     index += 1
#
# for index, string in enumerate(strings):
#     if 'xxx' in string:
#         string[index] = '[null]'


print [(x, y) for x in range(3) for y in range(3)]
