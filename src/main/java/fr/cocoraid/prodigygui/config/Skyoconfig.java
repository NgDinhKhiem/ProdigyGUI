package fr.cocoraid.prodigygui.config;

import com.google.common.base.Joiner;
import com.google.common.primitives.Primitives;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Skyoconfig {
   private static final transient char DEFAULT_SEPARATOR = '_';
   private static final transient String LINE_SEPARATOR = System.lineSeparator();
   private static final transient String TEMP_CONFIG_SECTION = "temp";
   private transient File configFile;
   private transient List<String> header;

   protected Skyoconfig(File configFile) {
      this(configFile, (List)null);
   }

   protected Skyoconfig(File configFile, List<String> header) {
      this.configFile = configFile;
      this.header = header;
   }

   public final void load() throws InvalidConfigurationException {
      try {
         YamlConfiguration config = YamlConfiguration.loadConfiguration(this.configFile);

         for(Class clazz = this.getClass(); clazz != Skyoconfig.class; clazz = clazz.getSuperclass()) {
            Field[] var3 = clazz.getFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Field field = var3[var5];
               this.loadField(field, this.getFieldName(field), config);
            }
         }

         this.saveConfig(config);
      } catch (Exception var7) {
         throw new InvalidConfigurationException(var7);
      }
   }

   public final void save() throws InvalidConfigurationException {
      try {
         YamlConfiguration config = YamlConfiguration.loadConfiguration(this.configFile);

         for(Class clazz = this.getClass(); clazz != Skyoconfig.class; clazz = clazz.getSuperclass()) {
            Field[] var3 = clazz.getFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Field field = var3[var5];
               this.saveField(field, this.getFieldName(field), config);
            }
         }

         this.saveConfig(config);
      } catch (Exception var7) {
         throw new InvalidConfigurationException(var7);
      }
   }

   private String getFieldName(Field field) {
      Skyoconfig.ConfigOptions options = (Skyoconfig.ConfigOptions)field.getAnnotation(Skyoconfig.ConfigOptions.class);
      if (options == null) {
         return field.getName().replace('_', '.');
      } else {
         String name = options.name();
         return name.equals("") ? field.getName().replace('_', '.') : name;
      }
   }

   private boolean ignoreField(Field field) {
      Skyoconfig.ConfigOptions options = (Skyoconfig.ConfigOptions)field.getAnnotation(Skyoconfig.ConfigOptions.class);
      return options != null && options.ignore();
   }

   private void saveConfig(YamlConfiguration config) throws IOException {
      if (this.header != null && this.header.size() > 0) {
         config.options().header(Joiner.on(LINE_SEPARATOR).join(this.header));
      }

      config.save(this.configFile);
   }

   private void loadField(Field field, String name, YamlConfiguration config) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException, InstantiationException {
      if (!Modifier.isTransient(field.getModifiers()) && !this.ignoreField(field)) {
         Object configValue = config.get(this.getFieldName(field));
         if (configValue == null) {
            this.saveField(field, name, config);
         } else {
            field.set(this, this.deserializeObject(field.getType(), configValue));
         }

      }
   }

   private void saveField(Field field, String name, YamlConfiguration config) throws IllegalAccessException {
      if (!Modifier.isTransient(field.getModifiers()) && !this.ignoreField(field)) {
         config.set(name, this.serializeObject(field.get(this), config));
      }
   }

   private Object deserializeObject(Class<?> clazz, Object object) throws ParseException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
      if (clazz.isPrimitive()) {
         return Primitives.wrap(clazz).getMethod("valueOf", String.class).invoke(this, object.toString());
      } else if (Primitives.isWrapperType(clazz)) {
         return clazz.getMethod("valueOf", String.class).invoke(this, object.toString());
      } else if (!clazz.isEnum() && !(object instanceof Enum)) {
         Object value;
         if (!Map.class.isAssignableFrom(clazz) && !(object instanceof Map)) {
            if (!List.class.isAssignableFrom(clazz) && !(object instanceof List)) {
               JSONObject jsonObject;
               if (!Location.class.isAssignableFrom(clazz) && !(object instanceof Location)) {
                  if (!Vector.class.isAssignableFrom(clazz) && !(object instanceof Vector)) {
                     return ChatColor.translateAlternateColorCodes('&', object.toString());
                  } else {
                     jsonObject = (JSONObject)(new JSONParser()).parse(object.toString());
                     return new Vector(Double.parseDouble(jsonObject.get("x").toString()), Double.parseDouble(jsonObject.get("y").toString()), Double.parseDouble(jsonObject.get("z").toString()));
                  }
               } else {
                  jsonObject = (JSONObject)(new JSONParser()).parse(object.toString());
                  return new Location(Bukkit.getWorld(jsonObject.get("world").toString()), Double.parseDouble(jsonObject.get("x").toString()), Double.parseDouble(jsonObject.get("y").toString()), Double.parseDouble(jsonObject.get("z").toString()), Float.parseFloat(jsonObject.get("yaw").toString()), Float.parseFloat(jsonObject.get("pitch").toString()));
               }
            } else {
               List<Object> result = new ArrayList();
               Iterator var10 = ((List)object).iterator();

               while(var10.hasNext()) {
                  value = var10.next();
                  result.add(this.deserializeObject(value.getClass(), value));
               }

               return result;
            }
         } else {
            ConfigurationSection section = (ConfigurationSection)object;
            Map<Object, Object> unserializedMap = new HashMap();
            Iterator var5 = section.getKeys(false).iterator();

            while(var5.hasNext()) {
               String key = (String)var5.next();
               Object object1 = section.get(key);
               unserializedMap.put(key, this.deserializeObject(object1.getClass(), object1));
            }

            value = clazz.newInstance();
            clazz.getMethod("putAll", Map.class).invoke(value, unserializedMap);
            return value;
         }
      } else {
         return clazz;
      }
   }

   private Object serializeObject(Object object, YamlConfiguration config) {
      if (object instanceof String) {
         return object.toString().replace('§', '&');
      } else if (object instanceof Enum) {
         return ((Enum)object).name();
      } else {
         Iterator var4;
         if (object instanceof Map) {
            ConfigurationSection section = config.createSection("temp");
            var4 = ((Map)object).entrySet().iterator();

            while(var4.hasNext()) {
               Entry<?, ?> entry = (Entry)var4.next();
               section.set(entry.getKey().toString(), this.serializeObject(entry.getValue(), config));
            }

            config.set("temp", (Object)null);
            return section;
         } else if (!(object instanceof List)) {
            JSONObject jsonObject;
            if (object instanceof Location) {
               Location location = (Location)object;
               jsonObject = new JSONObject();
               jsonObject.put("world", location.getWorld().getName());
               jsonObject.put("x", location.getX());
               jsonObject.put("y", location.getY());
               jsonObject.put("z", location.getZ());
               jsonObject.put("yaw", location.getYaw());
               jsonObject.put("pitch", location.getPitch());
               return jsonObject.toJSONString();
            } else if (object instanceof Vector) {
               Vector vector = (Vector)object;
               jsonObject = new JSONObject();
               jsonObject.put("x", vector.getX());
               jsonObject.put("y", vector.getY());
               jsonObject.put("z", vector.getZ());
               return jsonObject.toJSONString();
            } else {
               return object;
            }
         } else {
            List<Object> result = new ArrayList();
            var4 = ((List)object).iterator();

            while(var4.hasNext()) {
               Object value = var4.next();
               result.add(this.serializeObject(value, config));
            }

            return result;
         }
      }
   }

   public final List<String> getHeader() {
      return this.header;
   }

   public final File getFile() {
      return this.configFile;
   }

   public final void setHeader(List<String> header) {
      this.header = header;
   }

   public final void setFile(File configFile) {
      this.configFile = configFile;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.FIELD})
   protected @interface ConfigOptions {
      String name() default "";

      boolean ignore() default false;
   }
}
