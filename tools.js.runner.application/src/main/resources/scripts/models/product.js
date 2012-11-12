var Brand, Entity, Product, ProductModel,
  __hasProp = Object.prototype.hasOwnProperty,
  __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor; child.__super__ = parent.prototype; return child; };

Entity = (function() {

  function Entity(table) {
    this.table = table;
  }

  Entity.prototype.fields = function() {
    var fields, p;
    fields = [];
    for (p in this) {
      if (typeof this[p] !== 'function') fields.push(p);
    }
    return fields;
  };

  Entity.prototype.save = function() {
    var args, end, field, first, sql, start, _i, _len, _ref;
    this.createDate = utils.now();
    this.modifyDate = utils.now();
    start = "insert into " + this.table + " (";
    first = true;
    end = ") values (";
    args = [];
    _ref = this.fields();
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      field = _ref[_i];
      if (field === "table") continue;
      if (first) {
        first = false;
      } else {
        start += ",";
        end += ",";
      }
      start += field;
      end += "?";
      args.push(this[field]);
    }
    end += ")";
    sql = start + end;
    logger.info("The sql is '{}', and the args is {}", sql, sutils.toString(args));
    return db.up(sql, args);
  };

  Entity.prototype.remove = function() {
    var args, sql;
    sql = "delete from " + this.table + " where id = ?";
    args = [this.id];
    logger.info("The sql is '{}', and the args is {}", sql, sutils.toString(args));
    return db.update(sql, args);
  };

  Entity.prototype.update = function() {
    var args, field, sql, _i, _len, _ref;
    this.modifyDate = utils.now();
    sql = "update " + this.table + " ";
    args = [];
    _ref = this.fields();
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      field = _ref[_i];
      if (field === "table" || field === "id") continue;
      sql += "set " + field + " = ? ";
      args.push(this[field]);
    }
    sql += "where id = ?";
    args.push(this.id);
    logger.info("The sql is '{}', and the args is {}", sql, sutils.toString(args));
    return db.up(sql, args);
  };

  return Entity;

})();

Product = (function(_super) {

  __extends(Product, _super);

  function Product() {
    Product.__super__.constructor.call(this, "default_product");
  }

  return Product;

})(Entity);

Brand = (function(_super) {

  __extends(Brand, _super);

  function Brand(name) {
    this.name = name;
    Brand.__super__.constructor.call(this, "default_product_brand");
  }

  return Brand;

})(Entity);

ProductModel = (function(_super) {

  __extends(ProductModel, _super);

  function ProductModel() {
    ProductModel.__super__.constructor.call(this, "default_product_model");
  }

  return ProductModel;

})(Entity);
